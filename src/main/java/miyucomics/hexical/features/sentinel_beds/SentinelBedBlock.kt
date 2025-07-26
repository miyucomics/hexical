package miyucomics.hexical.features.sentinel_beds

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction

class SentinelBedBlock : FacingBlock(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 6f)) {
	init {
		this.defaultState = this.stateManager.getDefaultState().with(FACING, Direction.SOUTH)
	}

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		builder.add(FACING)
	}

	override fun mirror(state: BlockState, mirror: BlockMirror) = state.rotate(mirror.getRotation(state.get(FACING)))
	override fun rotate(state: BlockState, rotation: BlockRotation) = state.with(FACING, rotation.rotate(state.get(FACING)))
	override fun getPlacementState(context: ItemPlacementContext): BlockState = this.defaultState.with(FACING, context.playerLookDirection.opposite)
}