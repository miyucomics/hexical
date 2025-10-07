package miyucomics.hexical.features.patchouli

import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.features.flora.ConjureFloraRecipe
import net.minecraft.text.Style
import net.minecraft.world.World
import vazkii.patchouli.api.IComponentProcessor
import vazkii.patchouli.api.IVariable
import vazkii.patchouli.api.IVariableProvider

@Suppress("unused")
class ConjureFloraPatchouli : IComponentProcessor {
	var recipe: ConjureFloraRecipe? = null

	override fun setup(world: World, vars: IVariableProvider) {
		val id = vars["index"].asNumber().toInt()
		val recipes = world.recipeManager.listAllOfType(ConjureFloraRecipe.Type.INSTANCE)
		recipes.sortBy { it.cost }
		this.recipe = recipes[id]
	}

	override fun process(world: World, key: String): IVariable? {
		if (recipe == null)
			return null

		return when (key) {
			"input" -> IVariable.from(recipe!!.state)
			"cost" -> IVariable.from(costText(recipe!!.cost).setStyle(Style.EMPTY.withColor(ItemMediaHolder.HEX_COLOR)))
			else -> null
		}
	}
}