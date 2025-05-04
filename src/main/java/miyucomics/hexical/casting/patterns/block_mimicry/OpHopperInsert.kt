package miyucomics.hexical.casting.patterns.block_mimicry

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.ItemEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.math.min

class OpHopperInsert : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)

		val position = args.getBlockPos(1, argc)
		env.assertPosInRange(position)

		val dirRaw = args.getBlockPos(2, argc)
		val direction = Direction.fromVector(dirRaw.x, dirRaw.y, dirRaw.z) ?: throw MishapInvalidIota.of(args[2], 0, "axis_vector")

		return SpellAction.Result(Spell(item, position, direction), MediaConstants.DUST_UNIT, listOf(ParticleSpray.burst(item.pos, 1.0), ParticleSpray.burst(position.toCenterPos(), 1.0)))
	}

	private data class Spell(val item: ItemEntity, val position: BlockPos, val direction: Direction) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val new = insert(env.world, position, direction, item.stack)
			item.stack = new
		}
	}

	companion object {
		fun insert(world: World, targetPos: BlockPos, fromDirection: Direction, stack: ItemStack): ItemStack {
			var mutableStack = stack
			if (mutableStack.isEmpty)
				return mutableStack

			val blockEntity = world.getBlockEntity(targetPos)
			if (blockEntity !is Inventory)
				return mutableStack

			if (blockEntity is SidedInventory) {
				for (slot in blockEntity.getAvailableSlots(fromDirection)) {
					mutableStack = insertIntoSlot(blockEntity, slot, mutableStack)
					if (mutableStack.isEmpty)
						return ItemStack.EMPTY
				}
			} else {
				for (slot in 0 until blockEntity.size()) {
					mutableStack = insertIntoSlot(blockEntity, slot, mutableStack)
					if (mutableStack.isEmpty)
						return ItemStack.EMPTY
				}
			}

			return mutableStack
		}

		private fun insertIntoSlot(inventory: Inventory, slot: Int, stack: ItemStack): ItemStack {
			val targetStack = inventory.getStack(slot)
			if (!inventory.isValid(slot, stack))
				return stack

			if (targetStack.isEmpty) {
				inventory.setStack(slot, stack.copy())
				stack.count = 0
				inventory.markDirty()
				return ItemStack.EMPTY
			}

			if (!ItemStack.canCombine(stack, targetStack))
				return stack

			val maxInsert = min(stack.count, targetStack.maxCount - targetStack.count)
			if (maxInsert <= 0)
				return stack

			targetStack.increment(maxInsert)
			stack.decrement(maxInsert)
			inventory.markDirty()

			return stack
		}
	}
}