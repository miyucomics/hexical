package miyucomics.hexical.features.blocks

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.features.transmutation.TransmutationHelper
import miyucomics.hexical.features.transmutation.TransmutationResult
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.block.*
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
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
import net.minecraft.world.WorldAccess

class MediaJarBlock : Block(
	Settings
		.create()
		.emissiveLighting { _, _, _ -> true }
		.luminance { _ -> 15 }
		.sounds(BlockSoundGroup.GLASS)
		.nonOpaque()
), BlockEntityProvider, Waterloggable {
	init {
		this.defaultState = this.stateManager.getDefaultState().with(WATERLOGGED, false)
	}

	override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape = VoxelShapes.cuboid(3.0 / 16, 0.0, 3.0 / 16, 13.0 / 16, 14.0 / 16, 13.0 / 16)
	override fun createBlockEntity(pos: BlockPos, state: BlockState) = MediaJarBlockEntity(pos, state)

	override fun getPlacementState(itemPlacementContext: ItemPlacementContext): BlockState? {
		val fluidState = itemPlacementContext.world.getFluidState(itemPlacementContext.blockPos)
		return this.defaultState.with(LanternBlock.WATERLOGGED, fluidState.fluid === Fluids.WATER)
	}

	override fun onBreak(world: World, blockPos: BlockPos, blockState: BlockState?, playerEntity: PlayerEntity) {
		val blockEntity = world.getBlockEntity(blockPos)
		if (blockEntity is MediaJarBlockEntity) {
			val itemStack = ItemStack(HexicalBlocks.MEDIA_JAR_ITEM)
			blockEntity.setStackNbt(itemStack)
			val item = ItemEntity(world, blockPos.x.toDouble() + 0.5, blockPos.y.toDouble() + 0.5, blockPos.z.toDouble() + 0.5, itemStack)
			item.setToDefaultPickupDelay()
			world.spawnEntity(item)
		}

		super.onBreak(world, blockPos, blockState, playerEntity)
	}

	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		val jar = world.getBlockEntity(pos) as MediaJarBlockEntity
		val stack = player.getStackInHand(hand)
		player.swingHand(hand)

		return when (val result = TransmutationHelper.transmuteItem(world, stack, jar.getMedia(), jar::insertMedia, jar::withdrawMedia)) {
			is TransmutationResult.AbsorbedMedia -> {
				world.playSoundFromEntity(null, player, HexicalSounds.AMETHYST_MELT, SoundCategory.BLOCKS, 1f, 1f)
				ActionResult.SUCCESS
			}
			is TransmutationResult.TransmutedItems -> {
				world.playSound(null, pos, HexicalSounds.ITEM_DUNKS, SoundCategory.BLOCKS, 1f, 1f)
				result.output.forEach(player::giveItemStack)
				ActionResult.SUCCESS
			}
			is TransmutationResult.Pass -> ActionResult.FAIL
		}
	}

	override fun getStateForNeighborUpdate(blockState: BlockState, direction: Direction, blockState2: BlockState, worldAccess: WorldAccess, blockPos: BlockPos, blockPos2: BlockPos): BlockState {
		if (blockState.get(WATERLOGGED))
			worldAccess.scheduleFluidTick(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(worldAccess))
		return super.getStateForNeighborUpdate(blockState, direction, blockState2, worldAccess, blockPos, blockPos2)
	}

	override fun getFluidState(blockState: BlockState): FluidState? {
		if (blockState.get(WATERLOGGED))
			return Fluids.WATER.getStill(false)
		return super.getFluidState(blockState)
	}

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		builder.add(WATERLOGGED)
	}

	companion object {
		val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
		const val MAX_CAPACITY = MediaConstants.CRYSTAL_UNIT * 64
	}
}