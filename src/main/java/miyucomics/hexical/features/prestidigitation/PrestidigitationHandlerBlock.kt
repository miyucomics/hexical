package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
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
	}
}