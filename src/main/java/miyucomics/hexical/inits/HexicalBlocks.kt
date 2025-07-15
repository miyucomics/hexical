package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.blocks.*
import miyucomics.hexical.features.items.MediaJarItem
import miyucomics.hexical.features.media_jar.MediaJarBlock
import miyucomics.hexical.features.media_jar.MediaJarBlockEntity
import miyucomics.hexical.features.pedestal.PedestalBlock
import miyucomics.hexical.features.pedestal.PedestalBlockEntity
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.*
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.client.render.RenderLayer
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.DyeColor

object HexicalBlocks {
	val CONJURABLE_FLOWERS: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, HexicalMain.id("conjurable_flower"))

	val HEX_CANDLE_BLOCK: HexCandleBlock = HexCandleBlock()
	val HEX_CANDLE_CAKE_BLOCK: HexCandleCakeBlock = HexCandleCakeBlock()

	val MAGE_BLOCK: MageBlock = MageBlock()
	val MEDIA_JAR_BLOCK: MediaJarBlock = MediaJarBlock()

	@JvmField
	val CASTING_CARPET = DyedCarpetBlock(DyeColor.PURPLE, Settings.create().mapColor(MapColor.PURPLE).strength(0.1f).sounds(BlockSoundGroup.WOOL).burnable())
	val CASTING_CARPET_ITEM = BlockItem(CASTING_CARPET, Item.Settings())

	@JvmField
	val SENTINEL_BED_BLOCK: Block = Block(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 6f))

	val PERIWINKLE_FLOWER = FlowerbedBlock(Settings.create().mapColor(MapColor.PURPLE).noCollision().sounds(BlockSoundGroup.PINK_PETALS).pistonBehavior(PistonBehavior.DESTROY))
	val PERIWINKLE_FLOWER_ITEM = BlockItem(PERIWINKLE_FLOWER, Item.Settings())

	@JvmField
	val MEDIA_JAR_ITEM = MediaJarItem()
	val HEX_CANDLE_ITEM = BlockItem(HEX_CANDLE_BLOCK, Item.Settings())
	val SENTINEL_BED_ITEM = BlockItem(SENTINEL_BED_BLOCK, Item.Settings())

	@JvmField
	val PEDESTAL_BLOCK: PedestalBlock = PedestalBlock()
	val PEDESTAL_ITEM = BlockItem(PEDESTAL_BLOCK, Item.Settings())

	val HEX_CANDLE_BLOCK_ENTITY: BlockEntityType<HexCandleBlockEntity> = BlockEntityType.Builder.create(::HexCandleBlockEntity, HEX_CANDLE_BLOCK).build(null)
	val HEX_CANDLE_CAKE_BLOCK_ENTITY: BlockEntityType<HexCandleCakeBlockEntity> = BlockEntityType.Builder.create(::HexCandleCakeBlockEntity, HEX_CANDLE_CAKE_BLOCK).build(null)
	val MEDIA_JAR_BLOCK_ENTITY: BlockEntityType<MediaJarBlockEntity> = BlockEntityType.Builder.create(::MediaJarBlockEntity, MEDIA_JAR_BLOCK).build(null)
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)
	val PEDESTAL_BLOCK_ENTITY: BlockEntityType<PedestalBlockEntity> = BlockEntityType.Builder.create(::PedestalBlockEntity, PEDESTAL_BLOCK).build(null)

	fun init() {
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle_cake"), HEX_CANDLE_CAKE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("mage_block"), MAGE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("sentinel_bed"), SENTINEL_BED_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("periwinkle"), PERIWINKLE_FLOWER)
		Registry.register(Registries.BLOCK, HexicalMain.id("casting_carpet"), CASTING_CARPET)
		Registry.register(Registries.BLOCK, HexicalMain.id("pedestal"), PEDESTAL_BLOCK)

		Registry.register(Registries.ITEM, HexicalMain.id("mage_block"), BlockItem(MAGE_BLOCK, Item.Settings()))
		Registry.register(Registries.ITEM, HexicalMain.id("hex_candle"), HEX_CANDLE_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("sentinel_bed"), SENTINEL_BED_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("media_jar"), MEDIA_JAR_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("periwinkle"), PERIWINKLE_FLOWER_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("casting_carpet"), CASTING_CARPET_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("pedestal"), PEDESTAL_ITEM)

		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle_cake"), HEX_CANDLE_CAKE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("mage_block"), MAGE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("pedestal"), PEDESTAL_BLOCK_ENTITY)
	}

	fun clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlock(PERIWINKLE_FLOWER, RenderLayer.getCutout())
	}
}