package miyucomics.hexical.blocks

import at.petrak.hexcasting.common.lib.HexItems
import net.minecraft.block.*
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class MultislateBlock : MultifaceGrowthBlock(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 4f)), Waterloggable {
	init {
		this.defaultState = defaultState.with(WATERLOGGED, false)
	}

	private val grower = LichenGrower(this)

	override fun getStateForNeighborUpdate(blockState: BlockState, direction: Direction, blockState2: BlockState, worldAccess: WorldAccess, blockPos: BlockPos, blockPos2: BlockPos): BlockState {
		if (blockState.get(WATERLOGGED))
			worldAccess.scheduleFluidTick(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(worldAccess))
		return super.getStateForNeighborUpdate(blockState, direction, blockState2, worldAccess, blockPos, blockPos2)
	}

	override fun getFluidState(blockState: BlockState): FluidState {
		if (blockState.get(WATERLOGGED))
			return Fluids.WATER.getStill(false)
		return super.getFluidState(blockState)
	}

	override fun getGrower() = this.grower
	override fun isTransparent(blockState: BlockState, blockView: BlockView, blockPos: BlockPos) = false
	override fun canReplace(blockState: BlockState, itemPlacementContext: ItemPlacementContext) = !itemPlacementContext.shouldCancelInteraction() && itemPlacementContext.stack.isOf(HexItems.SLATE) && super.canReplace(blockState, itemPlacementContext)

	override fun withDirection(blockState: BlockState, blockView: BlockView, blockPos: BlockPos, direction: Direction?): BlockState {
		val new = if (blockState.isOf(this))
			blockState
		else if (blockState.fluidState.isEqualAndStill(Fluids.WATER))
			defaultState.with(Properties.WATERLOGGED, true)
		else
			this.defaultState
		return new.with(getProperty(direction), true)
	}

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		super.appendProperties(builder)
		builder.add(WATERLOGGED)
	}

	companion object {
		private val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
	}
}