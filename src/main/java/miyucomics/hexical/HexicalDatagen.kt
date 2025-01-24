package miyucomics.hexical

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.*

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {

	}
}

private class MyModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
	override fun generateBlockStateModels(generator: BlockStateModelGenerator) {

	}

	override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

	}
}