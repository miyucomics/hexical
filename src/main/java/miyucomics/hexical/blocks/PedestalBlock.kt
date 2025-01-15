package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent
import at.petrak.hexcasting.api.casting.circles.ICircleComponent.ControlFlow
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import miyucomics.hexical.blocks.HexCandleBlock.Companion.tick
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*

class PedestalBlock : BlockCircleComponent(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 4f)), BlockEntityProvider {
	init {
		defaultState = stateManager.defaultState
			.with(ENERGIZED, false)
			.with(FACING, Direction.NORTH)
	}

	override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
		val blockEntity = world.getBlockEntity(pos)
		if (blockEntity is PedestalBlockEntity && newState.block != this)
			blockEntity.onRemoved()
		super.onStateReplaced(state, world, pos, newState, moved)
	}

	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		val blockEntity = world.getBlockEntity(pos)
		if (blockEntity is PedestalBlockEntity)
			return blockEntity.onUse(player, hand)
		return ActionResult.PASS
	}

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		super.appendProperties(builder)
		builder.add(FACING)
	}

	override fun acceptControlFlow(imageIn: CastingImage, env: CircleCastEnv, enterDir: Direction, pos: BlockPos, state: BlockState, world: ServerWorld): ControlFlow {
		val exitDirsSet = this.possibleExitDirections(pos, state, world)
		exitDirsSet.remove(enterDir.opposite)
		val exitDirs = exitDirsSet.map { dir -> exitPositionFromDirection(pos, dir) }
		return ControlFlow.Continue(imageIn, exitDirs)
	}

	override fun canEnterFromDirection(enterDir: Direction?, pos: BlockPos?, state: BlockState?, world: ServerWorld?): Boolean {
		val thisNormal = this.normalDir(pos, state, world)
		return enterDir != thisNormal
	}

	override fun possibleExitDirections(pos: BlockPos?, state: BlockState?, world: World?): EnumSet<Direction> {
		val allDirs = EnumSet.allOf(Direction::class.java)
		val normal = this.normalDir(pos, state, world)
		allDirs.remove(normal)
		allDirs.remove(normal.opposite)
		return allDirs
	}

	override fun particleHeight(pos: BlockPos, state: BlockState, world: World) = PedestalBlockEntity.HEIGHT
	override fun normalDir(pos: BlockPos, state: BlockState, world: World, recursionLeft: Int): Direction = state.get(FACING)

	override fun getPlacementState(ctx: ItemPlacementContext): BlockState = super.getPlacementState(ctx)!!.with(FACING, ctx.side)
	override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) = SHAPES[state.get(FACING)]

	override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
		val blockEntity = world.getBlockEntity(pos)
		if (blockEntity !is PedestalBlockEntity)
			return 0
		return ScreenHandler.calculateComparatorOutput(blockEntity as Inventory)
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState) = PedestalBlockEntity(pos, state)
	override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T> = BlockEntityTicker { world1, pos, _, blockEntity -> (blockEntity as PedestalBlockEntity).tick(world1, pos) }

	companion object {
		val FACING: DirectionProperty = Properties.FACING
		val SHAPES = EnumMap(mapOf(
			Direction.NORTH to VoxelShapes.cuboid(0.0, 0.0, 0.25, 1.0, 1.0, 1.0),
			Direction.SOUTH to VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.75),
			Direction.EAST to VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.75, 1.0, 1.0),
			Direction.WEST to VoxelShapes.cuboid(0.25, 0.0, 0.0, 1.0, 1.0, 1.0),
			Direction.UP to VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.75, 1.0),
			Direction.DOWN to VoxelShapes.cuboid(0.0, 0.25, 0.0, 1.0, 1.0, 1.0)
		))
	}
}