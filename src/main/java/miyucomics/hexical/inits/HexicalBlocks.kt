package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.blocks.*
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
	val HEX_CANDLE_CAKE_BLOCK: HexCandleCakeBlock = HexCandleCakeBlock()

	val MAGE_BLOCK: MageBlock = MageBlock()
	val MEDIA_JAR_BLOCK: MediaJarBlock = MediaJarBlock()
	val PEDESTAL_BLOCK: PedestalBlock = PedestalBlock()

	@JvmField
	val SENTINEL_BED_BLOCK: Block = Block(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 6f))

	@JvmField
	val WITHERED_SLATE_BLOCK = WitheredSlateBlock()
	val FLAT_LOOKING_IMPETUS_BLOCK = FlatLookingImpetusBlock()
	val FLAT_REDSTONE_IMPETUS_BLOCK = FlatRedstoneImpetusBlock()
	val FLAT_RIGHT_CLICK_IMPETUS_BLOCK = FlatRightClickImpetusBlock()

	val HEX_CANDLE_BLOCK_ENTITY: BlockEntityType<HexCandleBlockEntity> = BlockEntityType.Builder.create(::HexCandleBlockEntity, HEX_CANDLE_BLOCK).build(null)
	val HEX_CANDLE_CAKE_BLOCK_ENTITY: BlockEntityType<HexCandleCakeBlockEntity> = BlockEntityType.Builder.create(::HexCandleCakeBlockEntity, HEX_CANDLE_CAKE_BLOCK).build(null)
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)
	val PEDESTAL_BLOCK_ENTITY: BlockEntityType<PedestalBlockEntity> = BlockEntityType.Builder.create(::PedestalBlockEntity, PEDESTAL_BLOCK).build(null)
	val FLAT_LOOKING_IMPETUS_BLOCK_ENTITY: BlockEntityType<FlatLookingImpetusBlockEntity> = BlockEntityType.Builder.create(::FlatLookingImpetusBlockEntity, FLAT_LOOKING_IMPETUS_BLOCK).build(null)
	val FLAT_REDSTONE_IMPETUS_BLOCK_ENTITY: BlockEntityType<FlatRedstoneImpetusBlockEntity> = BlockEntityType.Builder.create(::FlatRedstoneImpetusBlockEntity, FLAT_REDSTONE_IMPETUS_BLOCK).build(null)
	val FLAT_RIGHT_CLICK_IMPETUS_BLOCK_ENTITY: BlockEntityType<FlatRightClickImpetusBlockEntity> = BlockEntityType.Builder.create(::FlatRightClickImpetusBlockEntity, FLAT_RIGHT_CLICK_IMPETUS_BLOCK).build(null)

	@JvmStatic
	fun init() {
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_cake_candle"), HEX_CANDLE_CAKE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("mage_block"), MAGE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("pedestal"), PEDESTAL_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("sentinel_bed"), SENTINEL_BED_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("withered_slate"), WITHERED_SLATE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("flat_looking_impetus"), FLAT_LOOKING_IMPETUS_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("flat_redstone_impetus"), FLAT_REDSTONE_IMPETUS_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("flat_right_click_impetus"), FLAT_RIGHT_CLICK_IMPETUS_BLOCK)

		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_cake_candle"), HEX_CANDLE_CAKE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("mage_block"), MAGE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("pedestal"), PEDESTAL_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("flat_looking_impetus"), FLAT_LOOKING_IMPETUS_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("flat_redstone_impetus"), FLAT_REDSTONE_IMPETUS_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("flat_right_click_impetus"), FLAT_RIGHT_CLICK_IMPETUS_BLOCK_ENTITY)
	}

	@JvmStatic
	fun clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlock(MEDIA_JAR_BLOCK, RenderLayer.getCutout())
	}
}