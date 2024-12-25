package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.blocks.*
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalBlocks {
	val HEX_CANDLE_BLOCK: HexCandleBlock = HexCandleBlock()
	val MAGE_BLOCK: MageBlock = MageBlock()
	val MEDIA_JAR_BLOCK: MediaJarBlock = MediaJarBlock()

	val HEX_CANDLE_BLOCK_ENTITY: BlockEntityType<HexCandleBlockEntity> = BlockEntityType.Builder.create(::HexCandleBlockEntity, HEX_CANDLE_BLOCK).build(null)
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)

	@JvmStatic
	fun init() {
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("mage_block"), MAGE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK)

		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("mage_block"), MAGE_BLOCK_ENTITY)
	}

	@JvmStatic
	fun clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlock(MEDIA_JAR_BLOCK, RenderLayer.getCutout())
	}
}