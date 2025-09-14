package miyucomics.hexical.features.piston

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.piston.PistonHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.event.GameEvent

object BlockPusher {
	fun pushBlocks(world: ServerWorld, start: BlockPos, direction: Direction): Boolean {
		val pistonHandler = PistonHandler(world, start.offset(direction.opposite), direction, true)
		if (!pistonHandler.calculatePush())
			return false
		executePush(world, pistonHandler.movedBlocks, pistonHandler.brokenBlocks, direction)
		world.playSound(null, start, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.25f, world.random.nextFloat() * 0.25f + 0.6f)
		return true
	}

	private fun executePush(world: ServerWorld, toPush: List<BlockPos>, toBreak: List<BlockPos>, direction: Direction) {
		for (pos in toBreak)
			world.breakBlock(pos, true)

		val memory: MutableMap<BlockPos, BlockState> = HashMap()
		for (pos in toPush)
			memory[pos] = world.getBlockState(pos)
		for (pos in toPush)
			world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL or Block.MOVED)

		for (pos in toPush) {
			val state = memory[pos]!!
			val newPos = pos.offset(direction)
			world.setBlockState(newPos, state, Block.NOTIFY_ALL or Block.MOVED)
			world.updateNeighbors(newPos, state.block)
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, newPos, GameEvent.Emitter.of(state))
		}

		for (pos in toPush) {
			val state = memory[pos]!!
			val newPos = pos.offset(direction)
			state.prepare(world, newPos, 2)
			world.updateNeighborsAlways(newPos, state.block)
		}
	}
}