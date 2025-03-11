package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.pigment.ColorProvider
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.particles.ConjureParticleOptions
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.event.GameEvent

class HexCandleBlock : CandleBlock(
	Settings.create()
		.mapColor(MapColor.PURPLE)
		.nonOpaque()
		.strength(0.1f)
		.sounds(BlockSoundGroup.CANDLE)
		.luminance(STATE_TO_LUMINANCE)
		.pistonBehavior(PistonBehavior.DESTROY)
), BlockEntityProvider {
	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		val item = player.getStackInHand(hand)
		println(item.isIn(ItemTags.CANDLES))

		if (player.isSneaking)
			return super.onUse(state, world, pos, player, hand, hit)
		if (!state.get(AbstractCandleBlock.LIT))
			return super.onUse(state, world, pos, player, hand, hit)

		val stack = player.getStackInHand(hand)
		val candle = (world.getBlockEntity(pos)!! as HexCandleBlockEntity)
		if (IXplatAbstractions.INSTANCE.isPigment(stack))
			candle.setPigment(FrozenPigment(stack.copy(), player.uuid))
		else
			candle.setPigment(IXplatAbstractions.INSTANCE.getPigment(player))
		world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos)
		return ActionResult.SUCCESS
	}

	override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
		if (!state.get(AbstractCandleBlock.LIT))
			return

		val blockEntity = world.getBlockEntity(pos)
		if (blockEntity !is HexCandleBlockEntity)
			return

		val colorProvider = blockEntity.getPigment().colorProvider
		getParticleOffsets(state).forEach { offset: Vec3d -> spawnCandleParticles(world, Vec3d.of(pos).add(offset), random, colorProvider) }
	}

	override fun tryFillWithFluid(worldAccess: WorldAccess, blockPos: BlockPos, blockState: BlockState, fluidState: FluidState): Boolean {
		if (blockState.get(WATERLOGGED) || fluidState.fluid !== Fluids.WATER)
			return false
		worldAccess.setBlockState(blockPos, blockState.with(WATERLOGGED, true), 3)
		worldAccess.scheduleFluidTick(blockPos, fluidState.fluid, fluidState.fluid.getTickRate(worldAccess))
		return true
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HexCandleBlockEntity(pos, state)

	companion object {
		fun spawnCandleParticles(world: World, position: Vec3d, random: Random, colorProvider: ColorProvider) {
			if (random.nextFloat() < 0.17f)
				world.playSound(position.x + 0.5, position.y + 0.5, position.z + 0.5, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, true)
			world.addParticle(
				ConjureParticleOptions(colorProvider.getColor(world.time.toFloat(), position)),
				position.x, position.y, position.z,
				0.0, world.random.nextFloat() * 0.02, 0.0
			)
		}
	}
}