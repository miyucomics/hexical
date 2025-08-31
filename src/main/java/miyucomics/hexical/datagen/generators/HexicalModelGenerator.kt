package miyucomics.hexical.datagen.generators

import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.*

class HexicalModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
	override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
		generator.registerCubeAllModelTexturePool(HexicalBlocks.MAGE_BLOCK)
		generator.registerCandle(HexicalBlocks.HEX_CANDLE_BLOCK, HexicalBlocks.HEX_CANDLE_CAKE_BLOCK)
		generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(HexicalBlocks.SENTINEL_BED_BLOCK, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(HexicalBlocks.SENTINEL_BED_BLOCK))).coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()))
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