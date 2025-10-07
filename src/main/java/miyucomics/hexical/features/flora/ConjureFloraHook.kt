package miyucomics.hexical.features.flora

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.InitHook
import net.minecraft.block.Block
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.world.ServerWorld

object ConjureFloraHook : InitHook() {
	val CONJURE_FLORA_RECIPE: RecipeType<ConjureFloraRecipe> = Registry.register(Registries.RECIPE_TYPE, HexicalMain.id("conjure_flora"), ConjureFloraRecipe.Type.INSTANCE)
	fun getRecipe(world: ServerWorld, target: Block): ConjureFloraRecipe? = world.recipeManager.listAllOfType(CONJURE_FLORA_RECIPE).firstOrNull { it.state == target }

	override fun init() {
		Registry.register(Registries.RECIPE_SERIALIZER, HexicalMain.id("conjure_flora"), ConjureFloraSerializer.INSTANCE)
	}
}