package miyucomics.hexical.datagen

import miyucomics.hexical.datagen.generators.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		TransmutationProvider.init()
		generator.createPack().apply {
			addProvider(::HexicalAdvancementGenerator)
			addProvider(::HexicalBlockLootTableGenerator)
			addProvider(::HexicalModelGenerator)
			addProvider(::HexicalPatchouliGenerator)
			addProvider(::HexicalRecipeGenerator)
			addProvider(::HexicalDyeingGenerator)
		}
	}
}