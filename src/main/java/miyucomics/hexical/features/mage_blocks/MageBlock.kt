package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.common.blocks.BlockConjured
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.MapColor
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

class MageBlock : BlockConjured(
	Settings
		.create()
		.nonOpaque()
		.dropsNothing()
		.breakInstantly()
		.luminance { _ -> 2 }
		.mapColor(MapColor.CLEAR)
		.suffocates { _, _, _ -> false }
		.blockVision { _, _, _ -> false }
		.allowsSpawning { _, _, _, _ -> false }
		.sounds(BlockSoundGroup.AMETHYST_CLUSTER)
) {
	override fun emitsRedstonePower(state: BlockState) = true
	override fun getWeakRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
		val tile = world.getBlockEntity(pos)
		if (tile !is MageBlockEntity)
			return 0
		if (tile.properties["energized"]!!)
			return tile.redstone
		return 0
	}

	override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
		val tile = world.getBlockEntity(pos) as MageBlockEntity
		if (tile.properties["bouncy"]!!)
			entity.handleFallDamage(fallDistance, 0.0f, world.damageSources.fall())
		else
			super.onLandedUpon(world, state, pos, entity, fallDistance)
	}

	override fun onEntityLand(world: BlockView, entity: Entity) {
		val tile = world.getBlockEntity(entity.blockPos.add(0, -1, 0))
		if (tile !is MageBlockEntity)
			return
		if (tile.properties["bouncy"]!!) {
			val velocity = entity.velocity
			if (velocity.y < 0) {
				entity.setVelocity(velocity.x, -velocity.y, velocity.z)
				entity.fallDistance = 0f
			}
		} else
			super.onEntityLand(world, entity)
	}

	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		val tile = world.getBlockEntity(pos) as MageBlockEntity
		if (!tile.properties["replaceable"]!!)
			return ActionResult.PASS
		val stack = player.getStackInHand(hand)
		val item = stack.item
		if (item !is BlockItem)
			return ActionResult.PASS
		if (!player.isCreative)
			stack.decrement(1)
		world.playSound(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS, 1f, 1f, true)
		world.setBlockState(pos, item.block.getPlacementState(ItemPlacementContext(player, hand, stack, hit)))
		return ActionResult.SUCCESS
	}

	override fun onBreak(world: World, position: BlockPos, state: BlockState, player: PlayerEntity?) {
		val tile = world.getBlockEntity(position) as MageBlockEntity
		world.playSound(position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS, 1f, 1f, true)
		world.emitGameEvent(GameEvent.BLOCK_DESTROY, position, GameEvent.Emitter.of(player, state))
		world.setBlockState(position, Blocks.AIR.defaultState)
		world.removeBlockEntity(position)
		if (tile.properties["volatile"]!!) {
			for (offset in Direction.stream()) {
				val positionToTest = position.add(offset.vector)
				val otherState = world.getBlockState(positionToTest)
				val block = otherState.block
				if (block == HexicalBlocks.MAGE_BLOCK)
					block.onBreak(world, positionToTest, otherState, player)
			}
		}
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState) = MageBlockEntity(pos, state)
	override fun <T : BlockEntity> getTicker(pworld: World, pstate: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T> = BlockEntityTicker { world, position, state, blockEntity -> tick(world, position, state, blockEntity as MageBlockEntity) }

	companion object {
		fun tick(world: World, position: BlockPos, state: BlockState, blockEntity: MageBlockEntity) {
			if (blockEntity.properties["ephemeral"]!!) {
				blockEntity.lifespan--
				if (blockEntity.lifespan <= 0)
					HexicalBlocks.MAGE_BLOCK.onBreak(world, position, state, null)
			}
		}
	}
}