package miyucomics.hexical

import at.petrak.hexcasting.common.lib.HexItems
import com.ibm.icu.text.PluralRules
import miyucomics.hexical.registry.HexicalBlocks
import miyucomics.hexical.registry.HexicalItems
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.util.function.Consumer

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		val pack = generator.createPack()
		pack.addProvider(::HexicalModelGenerator)
		pack.addProvider(::HexicalRecipeGenerator)
	}
}

private class HexicalModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
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

class HexicalRecipeGenerator(generator: FabricDataOutput) : FabricRecipeProvider(generator) {
	override fun generate(exporter: Consumer<RecipeJsonProvider>) {
		HexicalItems.CURIOS.forEach { curio ->
			SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(HexItems.CHARGED_AMETHYST), RecipeCategory.MISC, curio, 1)
				.criterion(hasItem(HexItems.CHARGED_AMETHYST), conditionsFromItem(HexItems.CHARGED_AMETHYST))
				.offerTo(exporter, Identifier("items/" + Registries.ITEM.getId(curio).path + "_from_stonecutting"))
		}
	}
}
