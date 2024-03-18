package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.blocks.BlockConjured
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.MapColor
import net.minecraft.block.Material
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
import net.minecraft.util.math.Vec3i
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.event.GameEvent

class MageBlock : BlockConjured(
	Settings.of(Material.ORGANIC_PRODUCT).nonOpaque().dropsNothing().breakInstantly().luminance { _ -> 2 }
		.mapColor(MapColor.CLEAR).suffocates { _, _, _ -> false }.blockVision { _, _, _ -> false }
		.allowsSpawning { _, _, _, _ -> false }.sounds(BlockSoundGroup.AMETHYST_CLUSTER)
) {
	private val volatileOffsets: List<Vec3i> = listOf(
		Vec3i(-1, 0, 0), Vec3i(1, 0, 0),
		Vec3i(0, -1, 0), Vec3i(0, 1, 0),
		Vec3i(0, 0, -1), Vec3i(0, 0, 1),
	)

	override fun onEntityLand(world: BlockView, entity: Entity) {
		val pos = entity.blockPos.add(0, -1, 0)
		val tile = world.getBlockEntity(pos)
		if (tile !is MageBlockEntity)
			return
		if (tile.properties["bouncy"]!!) {
			val velocity = entity.velocity
			if (velocity.y < 0) {
				entity.setVelocity(velocity.x, -velocity.y, velocity.z)
				entity.fallDistance = 0f
			}
		} else {
			val velocity = entity.velocity
			entity.setVelocity(velocity.x, 0.0, velocity.z)
		}
	}

	override fun onUse(
		state: BlockState,
		world: World,
		pos: BlockPos,
		player: PlayerEntity,
		hand: Hand,
		hit: BlockHitResult
	): ActionResult {
		val tile = world.getBlockEntity(pos)
		if (tile !is MageBlockEntity)
			return ActionResult.PASS
		if (!tile.properties["replaceable"]!!)
			return ActionResult.PASS
		val stack = player.getStackInHand(hand)
		val item = stack.item
		if (item !is BlockItem)
			return ActionResult.PASS
		if (!player.isCreative)
			stack.decrement(1)
		world.playSound(
			pos.x.toDouble(),
			pos.y.toDouble(),
			pos.z.toDouble(),
			SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK,
			SoundCategory.BLOCKS,
			1f,
			1f,
			true
		)
		world.setBlockState(pos, item.block.getPlacementState(ItemPlacementContext(player, hand, stack, hit)))
		return ActionResult.SUCCESS
	}

	override fun onBreak(world: World, position: BlockPos, state: BlockState, player: PlayerEntity?) {
		val tile = world.getBlockEntity(position)
		if (tile !is MageBlockEntity)
			return
		world.playSound(
			position.x.toDouble(),
			position.y.toDouble(),
			position.z.toDouble(),
			SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK,
			SoundCategory.BLOCKS,
			1f,
			1f,
			true
		)
		world.emitGameEvent(GameEvent.BLOCK_DESTROY, position, GameEvent.Emitter.of(player, state))
		world.setBlockState(position, Blocks.AIR.defaultState)
		world.removeBlockEntity(position)
		if (tile.properties["volatile"]!!) {
			for (offset in volatileOffsets) {
				val positionToTest = position.add(offset)
				val otherState = world.getBlockState(positionToTest)
				val block = otherState.block
				if (block == HexicalBlocks.MAGE_BLOCK)
					block.onBreak(world, positionToTest, otherState, player)
			}
		}
	}

	override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
		val tile = world.getBlockEntity(pos)
		if (tile !is MageBlockEntity)
			return
		if (!tile.properties["invisible"]!!)
			tile.walkParticle(entity)
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return MageBlockEntity(pos, state)
	}

	override fun <T : BlockEntity?> getTicker(
		pworld: World,
		pstate: BlockState,
		type: BlockEntityType<T>
	): BlockEntityTicker<T> {
		return BlockEntityTicker { world, position, state, blockEntity: T -> tick(world, position, state, blockEntity) }
	}

	companion object {
		fun <T> tick(world: World, position: BlockPos, state: BlockState, blockEntity: T) {
			if (blockEntity !is MageBlockEntity)
				return
			if (!blockEntity.properties["invisible"]!!)
				blockEntity.particleEffect()
			if (blockEntity.properties["ephemeral"]!!) {
				blockEntity.lifespan--
				if (blockEntity.lifespan <= 0)
					HexicalBlocks.MAGE_BLOCK.onBreak(world, position, state, null)
			}
		}

		fun setProperty(world: WorldAccess, pos: BlockPos, property: String, args: List<Iota>) {
			val blockEntity = world.getBlockEntity(pos)
			if (blockEntity is MageBlockEntity)
				blockEntity.setProperty(property, args)
		}

		fun setColor(world: WorldAccess, pos: BlockPos, colorizer: FrozenColorizer) {
			val blockEntity = world.getBlockEntity(pos)
			if (blockEntity is MageBlockEntity)
				blockEntity.setColorizer(colorizer)
		}
	}
}