package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.blocks.*
import miyucomics.hexical.blocks.flat_impetii.FlatRightClickImpetus
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalBlocks {
	val HEX_CANDLE_BLOCK: HexCandleBlock = HexCandleBlock()
	val MAGE_BLOCK: MageBlock = MageBlock()
	val MEDIA_JAR_BLOCK: MediaJarBlock = MediaJarBlock()
	val PEDESTAL_BLOCK: PedestalBlock = PedestalBlock()

	@JvmField
	val SENTINEL_BED_BLOCK: Block = Block(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 6f))

	val HEX_CANDLE_BLOCK_ENTITY: BlockEntityType<HexCandleBlockEntity> = BlockEntityType.Builder.create(::HexCandleBlockEntity, HEX_CANDLE_BLOCK).build(null)
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)
	val PEDESTAL_BLOCK_ENTITY: BlockEntityType<PedestalBlockEntity> = BlockEntityType.Builder.create(::PedestalBlockEntity, PEDESTAL_BLOCK).build(null)

	@JvmField
	val WITHERED_SLATE = WitheredSlateBlock()
	val FLAT_RIGHT_CLICK_IMPETUS = FlatRightClickImpetus()

	@JvmStatic
	fun init() {
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("mage_block"), MAGE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("pedestal"), PEDESTAL_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("sentinel_bed"), SENTINEL_BED_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("withered_slate"), WITHERED_SLATE)
		Registry.register(Registries.BLOCK, HexicalMain.id("flat_right_click_impetus"), FLAT_RIGHT_CLICK_IMPETUS)

		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("mage_block"), MAGE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("pedestal"), PEDESTAL_BLOCK_ENTITY)
	}

	@JvmStatic
	fun clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlock(MEDIA_JAR_BLOCK, RenderLayer.getCutout())
	}
}