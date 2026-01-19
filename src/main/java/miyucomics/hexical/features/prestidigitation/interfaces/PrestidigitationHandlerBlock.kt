package miyucomics.hexical.features.prestidigitation.interfaces

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

abstract class PrestidigitationHandlerBlock : PrestidigitationHandler {
	abstract fun canAffectBlock(env: CastingEnvironment, pos: BlockPos): Boolean
	abstract fun affect(env: CastingEnvironment, pos: BlockPos)

	companion object {
		fun getBlockState(env: CastingEnvironment, pos: BlockPos): BlockState = env.world.getBlockState(pos)
		fun getBlock(env: CastingEnvironment, pos: BlockPos): Block = this.getBlockState(env, pos).block
		fun setBlockState(env: CastingEnvironment, pos: BlockPos, state: BlockState) {
			env.world.setBlockState(pos, state)
			env.world.updateNeighborsAlways(pos, state.block)
		}

		fun <T : BlockEntity> blockEntity(handledClass: Class<T>, effect: (ServerWorld, BlockPos, T) -> Unit) = object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = handledClass.isInstance(env.world.getBlockEntity(pos))
			override fun affect(env: CastingEnvironment, pos: BlockPos) = effect(env.world, pos, env.world.getBlockEntity(pos) as T)
		}
	}
}