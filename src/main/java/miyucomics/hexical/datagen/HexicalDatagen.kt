package miyucomics.hexical.datagen

import miyucomics.hexical.datagen.generators.*
import miyucomics.hexical.datagen.providers.FloraProvider
import miyucomics.hexical.datagen.providers.TransmutationProvider
import miyucomics.hexical.datagen.providers.dyeing.DyeingProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		DyeingProvider.init()
		FloraProvider.init()
		TransmutationProvider.init()

		generator.createPack().apply {
			addProvider(::HexicalAdvancementGenerator)
			addProvider(::HexicalBlockLootTableGenerator)
			addProvider(::HexicalDyeingGenerator)
			addProvider(::HexicalModelGenerator)
			addProvider(::HexicalPatchouliGenerator)
			addProvider(::HexicalRecipeGenerator)
		}
	}
}