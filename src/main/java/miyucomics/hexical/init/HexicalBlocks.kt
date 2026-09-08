package miyucomics.hexical.init

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.feature.hex_candle.HexCandleBlock
import miyucomics.hexical.feature.hex_candle.HexCandleBlockEntity
import miyucomics.hexical.feature.hex_candle.HexCandleCakeBlock
import miyucomics.hexical.feature.hex_candle.HexCandleCakeBlockEntity
import miyucomics.hexical.feature.media_jar.MediaJarBlock
import miyucomics.hexical.feature.media_jar.MediaJarBlockEntity
import miyucomics.hexical.feature.media_jar.MediaJarItem
import miyucomics.hexical.feature.pedestal.CarpetedPedestalBlock
import miyucomics.hexical.feature.pedestal.CarpetedPedestalBlockEntity
import miyucomics.hexical.feature.pedestal.PedestalBlock
import miyucomics.hexical.feature.pedestal.PedestalBlockEntity
import miyucomics.hexical.feature.sentinel_bed.SentinelBedBlock
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.DyedCarpetBlock
import net.minecraft.block.FlowerbedBlock
import net.minecraft.block.MapColor
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.DyeColor

object HexicalBlocks {
	val HEX_CANDLE_BLOCK: HexCandleBlock = HexCandleBlock()
	val HEX_CANDLE_CAKE_BLOCK: HexCandleCakeBlock = HexCandleCakeBlock()

	val MEDIA_JAR_BLOCK: MediaJarBlock = MediaJarBlock()

	@JvmField
	val CASTING_CARPET = DyedCarpetBlock(DyeColor.PURPLE, Settings.create().mapColor(MapColor.PURPLE).strength(0.1f).sounds(BlockSoundGroup.WOOL).burnable())
	val CASTING_CARPET_ITEM = BlockItem(CASTING_CARPET, Item.Settings())

	@JvmField
	val SENTINEL_BED_BLOCK = SentinelBedBlock()

	val PERIWINKLE_FLOWER = FlowerbedBlock(Settings.create().mapColor(MapColor.PURPLE).noCollision().sounds(BlockSoundGroup.PINK_PETALS).pistonBehavior(PistonBehavior.DESTROY))
	val PERIWINKLE_FLOWER_ITEM = BlockItem(PERIWINKLE_FLOWER, Item.Settings())

	val MEDIA_JAR_ITEM = MediaJarItem()
	val HEX_CANDLE_ITEM = BlockItem(HEX_CANDLE_BLOCK, Item.Settings())
	val SENTINEL_BED_ITEM = BlockItem(SENTINEL_BED_BLOCK, Item.Settings())

	val HEX_CANDLE_BLOCK_ENTITY: BlockEntityType<HexCandleBlockEntity> = BlockEntityType.Builder.create(::HexCandleBlockEntity, HEX_CANDLE_BLOCK).build(null)
	val HEX_CANDLE_CAKE_BLOCK_ENTITY: BlockEntityType<HexCandleCakeBlockEntity> = BlockEntityType.Builder.create(::HexCandleCakeBlockEntity, HEX_CANDLE_CAKE_BLOCK).build(null)
	val MEDIA_JAR_BLOCK_ENTITY: BlockEntityType<MediaJarBlockEntity> = BlockEntityType.Builder.create(::MediaJarBlockEntity, MEDIA_JAR_BLOCK).build(null)

	val PEDESTAL_BLOCK = PedestalBlock()
	val PEDESTAL_ITEM = BlockItem(PEDESTAL_BLOCK, Item.Settings())
	val PEDESTAL_BLOCK_ENTITY: BlockEntityType<PedestalBlockEntity> = BlockEntityType.Builder.create({ pos, state -> PedestalBlockEntity(PEDESTAL_BLOCK_ENTITY, pos, state) }, PEDESTAL_BLOCK).build(null)
	lateinit var CARPETED_PEDESTAL_BLOCK_ENTITY: BlockEntityType<CarpetedPedestalBlockEntity>

	val PEDESTAL_BLOCKS: MutableList<Pair<String, PedestalBlock>> = mutableListOf()
	val PEDESTAL_BLOCK_ITEMS: MutableList<BlockItem> = mutableListOf()

	fun init() {
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle_cake"), HEX_CANDLE_CAKE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("sentinel_bed"), SENTINEL_BED_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("periwinkle"), PERIWINKLE_FLOWER)
		Registry.register(Registries.BLOCK, HexicalMain.id("casting_carpet"), CASTING_CARPET)

		Registry.register(Registries.ITEM, HexicalMain.id("hex_candle"), HEX_CANDLE_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("sentinel_bed"), SENTINEL_BED_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("media_jar"), MEDIA_JAR_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("periwinkle"), PERIWINKLE_FLOWER_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("casting_carpet"), CASTING_CARPET_ITEM)

		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle_cake"), HEX_CANDLE_CAKE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK_ENTITY)

		PEDESTAL_BLOCKS.add(Pair("pedestal", PEDESTAL_BLOCK))
		Registry.register(Registries.BLOCK, HexicalMain.id("pedestal"), PEDESTAL_BLOCK)
		Registry.register(Registries.ITEM, HexicalMain.id("pedestal"), PEDESTAL_ITEM)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("pedestal"), PEDESTAL_BLOCK_ENTITY)

		DyeColor.entries.forEach { color ->
			val name = color.getName() + "_carpeted_pedestal"
			val id = HexicalMain.id(name)
			val block = CarpetedPedestalBlock()
			Registry.register(Registries.BLOCK, id, block)
			PEDESTAL_BLOCKS.add(Pair(name, block))
			PEDESTAL_BLOCK_ITEMS.add(Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings())))
		}

		CARPETED_PEDESTAL_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("carpeted_pedestal"), BlockEntityType.Builder.create(::CarpetedPedestalBlockEntity, *PEDESTAL_BLOCKS.map { (_, block) -> block }.toTypedArray()).build(null))
	}
}