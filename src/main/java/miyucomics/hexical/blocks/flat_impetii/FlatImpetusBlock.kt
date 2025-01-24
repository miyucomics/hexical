package miyucomics.hexical.blocks.flat_impetii

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
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
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.*

abstract class FlatImpetusBlock : BlockAbstractImpetus(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 4f)), Waterloggable {
	init {
		this.defaultState = stateManager.defaultState.with(ENERGIZED, false).with(FACING, Direction.NORTH).with(WATERLOGGED, false)
	}

	override fun getOutlineShape(state: BlockState, blockView: BlockView, blockPos: BlockPos, shapeContext: ShapeContext) = when (state.get(ATTACH_FACE)) {
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

	override fun getPlacementState(context: ItemPlacementContext): BlockState? {
		val fluidState = context.world.getFluidState(context.blockPos)
		for (direction in context.placementDirections) {
			var blockstate = if (direction.axis === Direction.Axis.Y) {
				defaultState.with(ATTACH_FACE, if (direction == Direction.UP) WallMountLocation.CEILING else WallMountLocation.FLOOR).with(FACING, context.horizontalPlayerFacing.opposite)
			} else {
				defaultState.with(ATTACH_FACE, WallMountLocation.WALL).with(FACING, context.horizontalPlayerFacing.opposite)
			}
			blockstate = blockstate.with(WATERLOGGED, fluidState.isIn(FluidTags.WATER) && fluidState.level == 8)
			if (blockstate.canPlaceAt(context.world, context.blockPos))
				return blockstate
		}
		return null
	}

	override fun normalDir(pos: BlockPos, state: BlockState, world: World, recursionLeft: Int): Direction = when (state.get(ATTACH_FACE)) {
		WallMountLocation.FLOOR -> Direction.UP
		WallMountLocation.CEILING -> Direction.DOWN
		WallMountLocation.WALL -> state.get(FACING)
		else -> throw IllegalStateException()
	}

	override fun canEnterFromDirection(enterDir: Direction, pos: BlockPos, state: BlockState, world: ServerWorld) = enterDir != state.get(FACING).opposite
	override fun possibleExitDirections(pos: BlockPos, state: BlockState, world: World): EnumSet<Direction> {
		if (state.get(ATTACH_FACE) == WallMountLocation.WALL)
			return EnumSet.of(Direction.DOWN)
		return EnumSet.of(state.get(FACING))
	}

	override fun getStateForNeighborUpdate(state: BlockState, direction: Direction, otherState: BlockState, world: WorldAccess, currentPosition: BlockPos, otherPosition: BlockPos): BlockState {
		if (state.get(WATERLOGGED))
			world.scheduleFluidTick(currentPosition, Fluids.WATER, Fluids.WATER.getTickRate(world))
		return if (getConnectedDirection(state).opposite == direction && !state.canPlaceAt(world, currentPosition))
			state.fluidState.blockState
		else
			super.getStateForNeighborUpdate(state, direction, otherState, world, currentPosition, otherPosition)
	}

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		super.appendProperties(builder)
		builder.add(ATTACH_FACE, WATERLOGGED)
	}

	abstract override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity
	override fun particleHeight(pos: BlockPos, state: BlockState, world: World) = 0.5f - 15f / 16f
	override fun isTransparent(state: BlockState, blockView: BlockView, blockPos: BlockPos) = !state.get(WATERLOGGED)
	override fun getFluidState(state: BlockState) = if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
	override fun canPlaceAt(state: BlockState, world: WorldView, position: BlockPos) = canAttach(world, position, getConnectedDirection(state).opposite)
	override fun rotate(state: BlockState, rot: BlockRotation) = state.with(FACING, rot.rotate(state.get(FACING)))
	override fun mirror(state: BlockState, mirror: BlockMirror) = state.rotate(mirror.getRotation(state.get(FACING)))

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

		private fun canAttach(world: WorldView, position: BlockPos, direction: Direction): Boolean {
			val testPosition = position.offset(direction)
			return world.getBlockState(testPosition).isSideSolidFullSquare(world, testPosition, direction.opposite)
		}

		private fun getConnectedDirection(state: BlockState) = when (state.get(ATTACH_FACE)) {
			WallMountLocation.CEILING -> Direction.DOWN
			WallMountLocation.FLOOR -> Direction.UP
			else -> state.get(FACING)
		}
	}
}