package miyucomics.hexical

import miyucomics.hexical.inits.HexicalItems
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.*

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		val pack = generator.createPack()
		pack.addProvider(::HexicalModelGenerator)
	}
}

private class HexicalModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
	override fun generateBlockStateModels(generator: BlockStateModelGenerator) {

	}

	override fun generateItemModels(generator: ItemModelGenerator) {
		generator.registerCompass(HexicalItems.CONJURED_COMPASS_ITEM)
	}
}