package miyucomics.hexical.inits

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.blocks.*
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object HexicalBlocks {
	val HEX_CANDLE_BLOCK: HexCandleBlock = HexCandleBlock()
	val HEX_CANDLE_CAKE_BLOCK: HexCandleCakeBlock = HexCandleCakeBlock()

	val MAGE_BLOCK: MageBlock = MageBlock()
	val MEDIA_JAR_BLOCK: MediaJarBlock = MediaJarBlock()
	val PEDESTAL_BLOCK: PedestalBlock = PedestalBlock()

	@JvmField
	val SENTINEL_BED_BLOCK: Block = Block(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 6f))

	val HEX_CANDLE_BLOCK_ENTITY: BlockEntityType<HexCandleBlockEntity> = BlockEntityType.Builder.create(::HexCandleBlockEntity, HEX_CANDLE_BLOCK).build(null)
	val HEX_CANDLE_CAKE_BLOCK_ENTITY: BlockEntityType<HexCandleCakeBlockEntity> = BlockEntityType.Builder.create(::HexCandleCakeBlockEntity, HEX_CANDLE_CAKE_BLOCK).build(null)
	val MEDIA_JAR_BLOCK_ENTITY: BlockEntityType<MediaJarBlockEntity> = BlockEntityType.Builder.create(::MediaJarBlockEntity, MEDIA_JAR_BLOCK).build(null)
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)
	val PEDESTAL_BLOCK_ENTITY: BlockEntityType<PedestalBlockEntity> = BlockEntityType.Builder.create(::PedestalBlockEntity, PEDESTAL_BLOCK).build(null)

	@JvmStatic
	fun init() {
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("hex_cake_candle"), HEX_CANDLE_CAKE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("mage_block"), MAGE_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("pedestal"), PEDESTAL_BLOCK)
		Registry.register(Registries.BLOCK, HexicalMain.id("sentinel_bed"), SENTINEL_BED_BLOCK)

		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_candle"), HEX_CANDLE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("hex_cake_candle"), HEX_CANDLE_CAKE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("media_jar"), MEDIA_JAR_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("mage_block"), MAGE_BLOCK_ENTITY)
		Registry.register(Registries.BLOCK_ENTITY_TYPE, HexicalMain.id("pedestal"), PEDESTAL_BLOCK_ENTITY)
	}

	@JvmStatic
	fun clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlock(MEDIA_JAR_BLOCK, RenderLayer.getCutout())
		BlockEntityRendererFactories.register(MEDIA_JAR_BLOCK_ENTITY, ::MediaJarBlockEntityRenderer)
		ScryingLensOverlayRegistry.addDisplayer(MEDIA_JAR_BLOCK) { lines: MutableList<Pair<ItemStack, Text>>, _: BlockState, pos: BlockPos, _: PlayerEntity, world: World, _: Direction -> (world.getBlockEntity(pos) as MediaJarBlockEntity).scryingLensOverlay(lines) }
	}
}