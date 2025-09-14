package miyucomics.hexical.datagen.generators

import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.datagen.TransmutationProvider
import miyucomics.hexical.inits.HexicalItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import java.util.function.Consumer

class HexicalRecipeGenerator(generator: FabricDataOutput) : FabricRecipeProvider(generator) {
	override fun generate(exporter: Consumer<RecipeJsonProvider>) {
		HexicalItems.CURIOS.forEach { curio ->
			SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(HexItems.CHARGED_AMETHYST), RecipeCategory.MISC, curio, 1)
				.criterion(hasItem(HexItems.CHARGED_AMETHYST), conditionsFromItem(HexItems.CHARGED_AMETHYST))
				.offerTo(exporter, Identifier("curio/${Registries.ITEM.getId(curio).path}_from_stonecutting"))
		}

		for (provider in TransmutationProvider.transmutationRecipeJsons)
			exporter.accept(provider)
	}
}