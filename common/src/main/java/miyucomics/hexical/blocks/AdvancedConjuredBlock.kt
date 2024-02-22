package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.misc.FrozenColorizer
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
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class AdvancedConjuredBlock : BlockConjured(Settings.of(Material.ORGANIC_PRODUCT).nonOpaque().dropsNothing().breakInstantly().luminance { _ -> 2 }.mapColor(MapColor.CLEAR).suffocates { _, _, _ -> false }.blockVision { _, _, _ -> false }.allowsSpawning { _, _, _, _ -> false }.sounds(BlockSoundGroup.AMETHYST_CLUSTER)) {
	private val offsets: List<Vec3i> = listOf(
		Vec3i(-1, 0, 0), Vec3i(1, 0, 0),
		Vec3i(0, -1, 0), Vec3i(0, 1, 0),
		Vec3i(0, 0, -1), Vec3i(0, 0, 1),
	)

	override fun onBreak(world: World, position: BlockPos, state: BlockState, player: PlayerEntity) {
		world.playSound(position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS, 1f, 1f, true)
		val tile = world.getBlockEntity(position)
		if (tile !is AdvancedConjuredBlockEntity)
			return
		if (!tile.volatile)
			return
		world.setBlockState(position, Blocks.AIR.defaultState)
		for (offset in offsets) {
			val positionToTest = position.add(offset);
			val otherState = world.getBlockState(positionToTest)
			val block = otherState.block
			if (block == HexicalBlocks.ADVANCED_CONJURED_BLOCK)
				block.onBreak(world, positionToTest, otherState, player)
		}
	}

	override fun onEntityLand(world: BlockView, entity: Entity) {
		val velocity = entity.velocity
		if (velocity.y < 0)
			entity.setVelocity(velocity.x, -velocity.y, velocity.z)
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return AdvancedConjuredBlockEntity(pos, state)
	}

	override fun <T : BlockEntity?> getTicker(world: World, state: BlockState?, type: BlockEntityType<T>?): BlockEntityTicker<T>? {
		return if (world.isClient) BlockEntityTicker { _, _, _, blockEntity: T -> tick(blockEntity) } else null
	}

	override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
		val tile = world.getBlockEntity(pos)
		if (tile is AdvancedConjuredBlockEntity)
			tile.walkParticle(entity)
	}

	companion object {
		fun <T> tick(blockEntity: T) {
			if (blockEntity is AdvancedConjuredBlockEntity)
				blockEntity.particleEffect()
		}

		fun setProperty(world: WorldAccess, pos: BlockPos, property: String) {
			val blockEntity = world.getBlockEntity(pos)
			if (blockEntity is AdvancedConjuredBlockEntity)
				blockEntity.setProperty(property)
		}

		fun setColor(world: WorldAccess, pos: BlockPos, colorizer: FrozenColorizer) {
			val blockEntity = world.getBlockEntity(pos)
			if (blockEntity is AdvancedConjuredBlockEntity)
				blockEntity.setColorizer(colorizer)
		}
	}
}