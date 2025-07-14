package miyucomics.hexical.datagen.generators

import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class HexicalModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
	override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
		generator.registerCandle(HexicalBlocks.HEX_CANDLE_BLOCK, HexicalBlocks.HEX_CANDLE_CAKE_BLOCK)
	}

	override fun generateItemModels(generator: ItemModelGenerator) {
		generator.registerCompass(HexicalItems.CONJURED_COMPASS_ITEM)
		for (curio in HexicalItems.CURIOS)
			generator.register(curio, Models.GENERATED)
		for (plushie in HexicalItems.PLUSHIES)
			generator.register(plushie, Models.GENERATED)
	}
}