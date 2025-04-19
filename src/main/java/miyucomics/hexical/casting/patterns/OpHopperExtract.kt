package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.casting.patterns.OpHopperInsert.Companion.insert
import net.minecraft.entity.ItemEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class OpHopperExtract : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val spawnPosition = args.getVec3(0, argc)
		env.assertVecInRange(spawnPosition)

		val inventoryPosition = args.getBlockPos(1, argc)
		env.assertPosInRange(inventoryPosition)

		val dirRaw = args.getBlockPos(2, argc)
		val direction = Direction.fromVector(dirRaw.x, dirRaw.y, dirRaw.z) ?: throw MishapInvalidIota.of(args[2], 0, "axis_vector")

		return SpellAction.Result(Spell(spawnPosition, inventoryPosition, direction), MediaConstants.DUST_UNIT, listOf(ParticleSpray.burst(spawnPosition, 1.0), ParticleSpray.burst(inventoryPosition.toCenterPos(), 1.0)))
	}

	private data class Spell(val spawnPosition: Vec3d, val inventoryPosition: BlockPos, val direction: Direction) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			val stack = extract(env.world, inventoryPosition, direction)
			if (stack.isEmpty) {
				val newStack = image.stack.toMutableList()
				newStack.add(NullIota())
				return image.copy(stack = newStack)
			}

			val item = ItemEntity(env.world, spawnPosition.x, spawnPosition.y, spawnPosition.z, stack)
			env.world.spawnEntity(item)
			val newStack = image.stack.toMutableList()
			newStack.add(EntityIota(item))
			return image.copy(stack = newStack)
		}
	}

	companion object {
		fun extract(world: World, pos: BlockPos, fromDirection: Direction): ItemStack {
			val blockEntity = world.getBlockEntity(pos) ?: return ItemStack.EMPTY
			if (blockEntity !is Inventory)
				return ItemStack.EMPTY

			println("Extracting")

			if (blockEntity is SidedInventory) {
				for (slot in blockEntity.getAvailableSlots(fromDirection)) {
					val stack = blockEntity.getStack(slot)
					if (!stack.isEmpty && blockEntity.canExtract(slot, stack, fromDirection)) {
						val extracted = stack.copy()
						extracted.count = 1
						stack.decrement(1)
						blockEntity.markDirty()
						return extracted
					}
				}
			} else {
				for (slot in 0 until blockEntity.size()) {
					val stack = blockEntity.getStack(slot)
					if (!stack.isEmpty) {
						val extracted = stack.copy()
						extracted.count = 1
						stack.decrement(1)
						blockEntity.markDirty()
						return extracted
					}
				}
			}

			return ItemStack.EMPTY
		}
	}
}