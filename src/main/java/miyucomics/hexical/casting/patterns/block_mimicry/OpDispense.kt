package miyucomics.hexical.casting.patterns.block_mimicry

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.mixin.DispenserBlockInvoker
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.DispenserBlock
import net.minecraft.block.dispenser.DispenserBehavior
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.DispenserBlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ArrowItem
import net.minecraft.util.math.BlockPointerImpl
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class OpDispense : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		val stack = item.stack
		val behavior = (Blocks.DISPENSER as DispenserBlockInvoker).invokeGetBehaviorForItem(stack)
		if (behavior == DispenserBehavior.NOOP || stack.isEmpty)
			return SpellAction.Result(Noop(item), 0, listOf())

		var cost = MediaConstants.DUST_UNIT / 2
		if (stack.item is ArrowItem)
			cost = MediaConstants.CRYSTAL_UNIT

		val position = args.getBlockPos(1, argc)
		env.assertPosInRange(position)

		val dirRaw = args.getBlockPos(2, argc)
		val direction = Direction.fromVector(dirRaw.x, dirRaw.y, dirRaw.z) ?: throw MishapInvalidIota.of(args[2], 0, "axis_vector")

		return SpellAction.Result(Spell(item, behavior, position, direction), cost, listOf())
	}

	private data class Noop(val item: ItemEntity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
	}

	private data class Spell(val item: ItemEntity, val behavior: DispenserBehavior, val position: BlockPos, val direction: Direction) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = item.stack
			val fakeDispenser: BlockState = Blocks.DISPENSER.defaultState.with(DispenserBlock.FACING, direction)
			val blockEntity = DispenserBlockEntity(position, fakeDispenser)

			val blockPointer = object : BlockPointerImpl(env.world, blockEntity.pos) {
				override fun <T : BlockEntity> getBlockEntity() = blockEntity as T
				override fun getBlockState() = fakeDispenser
			}

			item.stack = behavior.dispense(blockPointer, stack)

			val spawnPos = position.toCenterPos()
			for (i in 0 until blockEntity.size()) {
				val leftoverStack = blockEntity.getStack(i)
				if (leftoverStack.isEmpty)
					continue
				env.world.spawnEntity(ItemEntity(env.world, spawnPos.x, spawnPos.y, spawnPos.z, leftoverStack))
			}
		}
	}
}