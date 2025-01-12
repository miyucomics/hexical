package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent
import at.petrak.hexcasting.api.casting.circles.ICircleComponent.ControlFlow
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
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
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
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
			return blockEntity.use(player, hand, hit)
		return ActionResult.PASS
	}


	override fun onSyncedBlockEvent(state: BlockState, world: World, pos: BlockPos, type: Int, data: Int): Boolean {
		super.onSyncedBlockEvent(state, world, pos, type, data)
		val blockEntity = world.getBlockEntity(pos) ?: return false
		return blockEntity.onSyncedBlockEvent(type, data)
	}

	override fun createScreenHandlerFactory(state: BlockState, world: World, pos: BlockPos): NamedScreenHandlerFactory? {
		val blockEntity = world.getBlockEntity(pos)
		return if (blockEntity is NamedScreenHandlerFactory) blockEntity else null
	}

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		super.appendProperties(builder)
		builder.add(FACING)
	}

	override fun acceptControlFlow(imageIn: CastingImage, env: CircleCastEnv, enterDir: Direction, pos: BlockPos, bs: BlockState, world: ServerWorld): ControlFlow {
		val exitDirsSet = this.possibleExitDirections(pos, bs, world)
		exitDirsSet.remove(enterDir.opposite)
		val exitDirs = exitDirsSet.map { dir: Direction -> exitPositionFromDirection(pos, dir) }
		return ControlFlow.Continue(imageIn, exitDirs)
	}

	override fun canEnterFromDirection(enterDir: Direction?, pos: BlockPos?, bs: BlockState?, world: ServerWorld?): Boolean {
		val thisNormal = this.normalDir(pos, bs, world)
		return enterDir != thisNormal
	}

	override fun possibleExitDirections(pos: BlockPos?, bs: BlockState?, world: World?): EnumSet<Direction> {
		val allDirs = EnumSet.allOf(Direction::class.java)
		val normal = this.normalDir(pos, bs, world)
		allDirs.remove(normal)
		allDirs.remove(normal.opposite)
		return allDirs
	}

	override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
	override fun particleHeight(pos: BlockPos, bs: BlockState, world: World) = PedestalBlockEntity.HEIGHT
	override fun normalDir(pos: BlockPos, bs: BlockState, world: World, recursionLeft: Int): Direction = bs.get(FACING)

	override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
		if (type != HexicalBlocks.PEDESTAL_BLOCK_ENTITY)
			return null
		return BlockEntityTicker { world2, position, state2, pedestal -> (pedestal as PedestalBlockEntity).tick(world2, position, state2) }
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState) = PedestalBlockEntity(pos, state)

	override fun getPlacementState(ctx: ItemPlacementContext): BlockState = super.getPlacementState(ctx)!!.with(FACING, ctx.side)
	override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) = SHAPES[state.get(FACING)]

	override fun hasComparatorOutput(state: BlockState) = true
	override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
		val blockEntity = world.getBlockEntity(pos, HexicalBlocks.PEDESTAL_BLOCK_ENTITY).orElse(null) ?: return 0
		return ScreenHandler.calculateComparatorOutput(blockEntity as Inventory)
	}

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