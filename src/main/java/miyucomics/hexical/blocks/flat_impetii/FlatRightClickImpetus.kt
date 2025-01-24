package miyucomics.hexical.blocks.flat_impetii

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import at.petrak.hexcasting.common.blocks.circles.impetuses.BlockRightClickImpetus
import miyucomics.hexical.blocks.WitheredSlateBlock.Companion.getConnectedDirection
import net.minecraft.block.*
import net.minecraft.block.enums.WallMountLocation
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.FluidTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.*

class FlatRightClickImpetus : BlockRightClickImpetus(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 4f)), Waterloggable {
	init {
		this.defaultState = stateManager.defaultState.with(ENERGIZED, false).with(FACING, Direction.NORTH).with(WATERLOGGED, false)
	}

	override fun isTransparent(state: BlockState, blockView: BlockView, blockPos: BlockPos) = !state.get(WATERLOGGED)
	override fun getFluidState(state: BlockState) = if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)

	override fun getOutlineShape(state: BlockState, blockView: BlockView, blockPos: BlockPos, shapeContext: ShapeContext): VoxelShape {
		return when (state.get(ATTACH_FACE)) {
			WallMountLocation.FLOOR -> AABB_FLOOR
			WallMountLocation.CEILING -> AABB_CEILING
			WallMountLocation.WALL -> when (state.get(FACING)) {
				Direction.NORTH -> AABB_NORTH_WALL
				Direction.EAST -> AABB_EAST_WALL
				Direction.SOUTH -> AABB_SOUTH_WALL
				else -> AABB_WEST_WALL
			}
			else -> throw IllegalStateException()
		}
	}

	override fun normalDir(pos: BlockPos, state: BlockState, world: World, recursionLeft: Int): Direction = when (state.get(ATTACH_FACE)) {
		WallMountLocation.FLOOR -> Direction.UP
		WallMountLocation.CEILING -> Direction.DOWN
		WallMountLocation.WALL -> state.get(FACING)
		else -> throw IllegalStateException()
	}


	override fun getPlacementState(pContext: ItemPlacementContext): BlockState? {
		val fluidState = pContext.world.getFluidState(pContext.blockPos)
		for (direction in pContext.placementDirections) {
			var blockstate = if (direction.axis === Direction.Axis.Y) {
				defaultState.with(ATTACH_FACE, if (direction == Direction.UP) WallMountLocation.CEILING else WallMountLocation.FLOOR).with(FACING, pContext.horizontalPlayerFacing.opposite)
			} else {
				defaultState.with(ATTACH_FACE, WallMountLocation.WALL).with(FACING, pContext.horizontalPlayerFacing.opposite)
			}
			blockstate = blockstate.with(WATERLOGGED, fluidState.isIn(FluidTags.WATER) && fluidState.level == 8)
			if (blockstate.canPlaceAt(pContext.world, pContext.blockPos))
				return blockstate
		}
		return null
	}

	override fun canEnterFromDirection(enterDir: Direction, pos: BlockPos, bs: BlockState, world: ServerWorld) = enterDir != bs.get(FACING).opposite
	override fun possibleExitDirections(pos: BlockPos, bs: BlockState, world: World): EnumSet<Direction> {
		if (bs.get(ATTACH_FACE) == WallMountLocation.WALL)
			return EnumSet.of(Direction.DOWN)
		return EnumSet.of(bs.get(FACING))
	}
	override fun particleHeight(pos: BlockPos, bs: BlockState, world: World) = 0.5f - 15f / 16f

	override fun rotate(state: BlockState, rot: BlockRotation) = state.with(BlockAbstractImpetus.FACING, rot.rotate(state.get(FACING)))
	override fun mirror(state: BlockState, mirror: BlockMirror) = state.rotate(mirror.getRotation(state.get(FACING)))

	override fun canPlaceAt(state: BlockState, pLevel: WorldView, pPos: BlockPos): Boolean {
		return canAttach(pLevel, pPos, getConnectedDirection(state).opposite)
	}

	override fun getStateForNeighborUpdate(pState: BlockState, pFacing: Direction, pFacingState: BlockState?, pLevel: WorldAccess, pCurrentPos: BlockPos?, pFacingPos: BlockPos?): BlockState {
		if (pState.get(WATERLOGGED)) {
			pLevel.scheduleFluidTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickRate(pLevel))
		}

		return if (getConnectedDirection(pState).opposite == pFacing && !pState.canPlaceAt(pLevel, pCurrentPos))
			pState.fluidState.blockState
		else
			super.getStateForNeighborUpdate(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos)
	}

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		super.appendProperties(builder)
		builder.add(ATTACH_FACE, WATERLOGGED)
	}

	companion object {
		val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
		val ATTACH_FACE: EnumProperty<WallMountLocation> = Properties.WALL_MOUNT_LOCATION

		private const val THICKNESS: Double = 1.0
		val AABB_FLOOR: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, THICKNESS, 16.0)
		val AABB_CEILING: VoxelShape = createCuboidShape(0.0, 16 - THICKNESS, 0.0, 16.0, 16.0, 16.0)
		val AABB_EAST_WALL: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, THICKNESS, 16.0, 16.0)
		val AABB_WEST_WALL: VoxelShape = createCuboidShape(16 - THICKNESS, 0.0, 0.0, 16.0, 16.0, 16.0)
		val AABB_SOUTH_WALL: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, THICKNESS)
		val AABB_NORTH_WALL: VoxelShape = createCuboidShape(0.0, 0.0, 16 - THICKNESS, 16.0, 16.0, 16.0)

		private fun canAttach(pReader: WorldView, pPos: BlockPos, pDirection: Direction): Boolean {
			val blockpos = pPos.offset(pDirection)
			return pReader.getBlockState(blockpos).isSideSolidFullSquare(pReader, blockpos, pDirection.opposite)
		}

		private fun getConnectedDirection(pState: BlockState): Direction {
			return when (pState.get(ATTACH_FACE)) {
				WallMountLocation.CEILING -> Direction.DOWN
				WallMountLocation.FLOOR -> Direction.UP
				else -> pState.get(FACING)
			}
		}
	}
}