package miyucomics.hexical.datagen

import miyucomics.hexical.datagen.generators.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		TransmutationProvider.init()

		val pack = generator.createPack()
		pack.addProvider(::HexicalAdvancementGenerator)
		pack.addProvider(::HexicalBlockLootTableGenerator)
		pack.addProvider(::HexicalModelGenerator)
		pack.addProvider(::HexicalPatchouliGenerator)
		pack.addProvider(::HexicalRecipeGenerator)
	}
}