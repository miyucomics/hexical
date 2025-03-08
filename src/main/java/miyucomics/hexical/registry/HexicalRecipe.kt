package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.recipe.TransmutingRecipe
import miyucomics.hexical.recipe.TransmutingSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalRecipe {
	lateinit var TRANSMUTING_RECIPE: RecipeType<TransmutingRecipe>

	@JvmStatic
	fun init() {
		Registry.register(Registries.RECIPE_SERIALIZER, HexicalMain.id("transmuting"), TransmutingSerializer.INSTANCE)
		TRANSMUTING_RECIPE = Registry.register(Registries.RECIPE_TYPE, HexicalMain.id("transmuting"), TransmutingRecipe.Type.INSTANCE)
	}
}