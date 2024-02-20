package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.blocks.ConjuredBouncyBlock
import miyucomics.hexical.blocks.ConjuredBouncyBlockEntity
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry

object HexicalBlocks {
	private val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_KEY)
	private val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY)
	val CONJURED_BOUNCY_BLOCK: Block = ConjuredBouncyBlock(
		AbstractBlock.Settings
			.of(Material.ORGANIC_PRODUCT)
			.nonOpaque()
			.dropsNothing()
			.breakInstantly()
			.luminance { _ -> 2 }
			.mapColor(MapColor.CLEAR)
			.suffocates { _, _, _ -> false }
			.blockVision { _, _, _ -> false }
			.allowsSpawning { _, _, _, _ -> false }
			.sounds(BlockSoundGroup.AMETHYST_CLUSTER)
	)
	val CONJURED_BOUNCY_BLOCK_ENTITY: BlockEntityType<ConjuredBouncyBlockEntity> = BlockEntityType.Builder.create(::ConjuredBouncyBlockEntity, CONJURED_BOUNCY_BLOCK).build(null)

	@JvmStatic
	fun init() {
		BLOCKS.register("conjured_bouncy_block") { CONJURED_BOUNCY_BLOCK }
		BLOCK_ENTITIES.register("conjured_bouncy_block") { CONJURED_BOUNCY_BLOCK_ENTITY }
		BLOCK_ENTITIES.register()
		BLOCKS.register()
	}
}