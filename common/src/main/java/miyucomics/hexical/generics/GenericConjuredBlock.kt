package miyucomics.hexical.generics

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.common.blocks.BlockConjured
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

abstract class GenericConjuredBlock<T : BlockEntity>(properties: Settings, private val blockEntityProvider: (BlockPos, BlockState) -> T) : BlockConjured(properties) {
	override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return blockEntityProvider(pos, state)
	}

	override fun <T : BlockEntity?> getTicker(world: World, state: BlockState?, type: BlockEntityType<T>?): BlockEntityTicker<T>? {
		return if (world.isClient) BlockEntityTicker { _, _, _, blockEntity: T -> tick(blockEntity) } else null
	}

	override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
		val tile = world.getBlockEntity(pos)
		if (tile is GenericConjuredBlockEntity<*>)
			tile.walkParticle(entity)
	}

	companion object {
		fun <T> tick(blockEntity: T) {
			if (blockEntity is GenericConjuredBlockEntity<*>)
				blockEntity.particleEffect()
		}

		fun setColor(world: WorldAccess, pos: BlockPos, colorizer: FrozenColorizer) {
			val blockEntity = world.getBlockEntity(pos)
			if (blockEntity is GenericConjuredBlockEntity<*>)
				blockEntity.setColorizer(colorizer)
		}

		fun baseMaterial(): Settings {
			return Settings.of(Material.ORGANIC_PRODUCT).nonOpaque().dropsNothing().breakInstantly().luminance { _ -> 2 }.mapColor(MapColor.CLEAR).suffocates { _, _, _ -> false }.blockVision { _, _, _ -> false }.allowsSpawning { _, _, _, _ -> false }.sounds(BlockSoundGroup.AMETHYST_CLUSTER);
		}
	}
}