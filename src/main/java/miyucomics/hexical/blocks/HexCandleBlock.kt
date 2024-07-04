package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.common.particles.ConjureParticleOptions
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
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
import net.minecraft.world.event.GameEvent

class HexCandleBlock : CandleBlock(Settings.of(Material.DECORATION).nonOpaque().strength(0.1f).sounds(BlockSoundGroup.CANDLE).luminance(STATE_TO_LUMINANCE)), BlockEntityProvider {
	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult?): ActionResult {
		if (player.isSneaking)
			return super.onUse(state, world, pos, player, hand, hit)
		if (!state.get(AbstractCandleBlock.LIT))
			return super.onUse(state, world, pos, player, hand, hit)
		val stack = player.getStackInHand(hand)
		val be = world.getBlockEntity(pos)!! as HexCandleBlockEntity
		var givenColor = IXplatAbstractions.INSTANCE.getColorizer(player)
		if (IXplatAbstractions.INSTANCE.isColorizer(stack))
			givenColor = FrozenColorizer(stack.copy(), player.uuid)
		be.setPigment(givenColor)
		world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos)
		return ActionResult.SUCCESS
	}

	override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
		if (!state.get(AbstractCandleBlock.LIT))
			return
		getParticleOffsets(state).forEach { offset: Vec3d ->
			val position = offset.add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
			if (random.nextFloat() < 0.25f)
				world.playSound(position.x + 0.5, position.y + 0.5, position.z + 0.5, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, false)
		}
	}

	fun getPositions(state: BlockState): Iterable<Vec3d> {
		return getParticleOffsets(state)
	}

	override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity = HexCandleBlockEntity(pos, state)
	override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T> = BlockEntityTicker { world1, pos, state1, blockEntity -> tick(world1, pos, state1, blockEntity as HexCandleBlockEntity) }

	companion object {
		fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: HexCandleBlockEntity) {
			if (!world.isClient)
				return
			if (!state.get(AbstractCandleBlock.LIT))
				return
			val pigment = blockEntity.getPigment()
			(state.block as HexCandleBlock).getPositions(state).forEach { offset: Vec3d ->
				if (world.random.nextFloat() > 0.5)
					return@forEach
				val position = offset.add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
				world.addParticle(
					ConjureParticleOptions(pigment.getColor(world.time.toFloat(), position), true),
					position.x, position.y, position.z,
					0.0, world.random.nextFloat() * 0.02, 0.0
				)
			}
		}
	}
}