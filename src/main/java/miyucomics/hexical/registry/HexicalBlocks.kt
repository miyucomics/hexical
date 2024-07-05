package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.blocks.*
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.registry.Registry

object HexicalBlocks {
	val HEX_CANDLE_BLOCK: HexCandleBlock = HexCandleBlock()
	val HEX_CANDLE_BLOCK_ENTITY: BlockEntityType<HexCandleBlockEntity> = BlockEntityType.Builder.create(::HexCandleBlockEntity, HEX_CANDLE_BLOCK).build(null)
	val MAGE_BLOCK: MageBlock = MageBlock()
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)
	val MEDIA_JAR_BLOCK: MediaJarBlock = MediaJarBlock()

	@JvmStatic
	fun init() {
		Registry.register(Registry.BLOCK, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK)
		Registry.register(Registry.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK_ENTITY)
		Registry.register(Registry.BLOCK, HexicalMain.id("mage_block"), MAGE_BLOCK)
		Registry.register(Registry.BLOCK_ENTITY_TYPE, HexicalMain.id("mage_block"), MAGE_BLOCK_ENTITY)
		Registry.register(Registry.BLOCK, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK)
	}

	@JvmStatic
	fun clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlock(MEDIA_JAR_BLOCK, RenderLayer.getTranslucent())
	}
}