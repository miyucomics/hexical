package miyucomics.hexical.datagen.generators

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.*
import net.minecraft.data.client.VariantSettings.Rotation
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.*

class HexicalModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
	override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
		generator.registerSimpleCubeAll(HexicalBlocks.MAGE_BLOCK)
		generator.registerCandle(HexicalBlocks.HEX_CANDLE_BLOCK, HexicalBlocks.HEX_CANDLE_CAKE_BLOCK)
		generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(HexicalBlocks.SENTINEL_BED_BLOCK, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(HexicalBlocks.SENTINEL_BED_BLOCK))).coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()))

		val template = Model(Optional.of(HexicalMain.id("block/pedestal_base")), Optional.empty(), TextureKey.SIDE, TextureKey.TOP, TextureKey.BOTTOM)
		for ((name, pedestal) in HexicalBlocks.PEDESTAL_BLOCKS) {
			val modelId: Identifier = template.upload(pedestal, TextureMap().put(TextureKey.SIDE, Identifier.of("hexical", "block/" + name + "_side")).put(TextureKey.TOP, Identifier.of("hexical", "block/" + name + "_top")).put(TextureKey.BOTTOM, Identifier.of("hexical", "block/pedestal_bottom")), generator.modelCollector)

			generator.blockStateCollector.accept(
				VariantsBlockStateSupplier.create(pedestal)
					.coordinate(BlockStateVariantMap.create(Properties.FACING)
						.register(Direction.UP, BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
						.register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.X, Rotation.R180))
						.register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.X, Rotation.R90))
						.register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.X, Rotation.R90).put(VariantSettings.Y, Rotation.R90))
						.register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.X, Rotation.R90).put(VariantSettings.Y, Rotation.R180))
						.register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.X, Rotation.R90).put(VariantSettings.Y, Rotation.R270)))
			)

			generator.registerParentedItemModel(pedestal, modelId)
		}
	}

	override fun generateItemModels(generator: ItemModelGenerator) {
		for (curio in HexicalItems.CURIOS) {
			when (curio) {
				HexicalItems.CURIO_COMPASS -> generator.registerCompass(curio)
				HexicalItems.CURIO_STAFF -> generator.register(curio, Models.HANDHELD_ROD)
				else -> generator.register(curio, Models.GENERATED)
			}
		}

		for (plushie in HexicalItems.PLUSHIES)
			generator.register(plushie, Models.GENERATED)
	}
}