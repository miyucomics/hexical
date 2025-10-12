package miyucomics.hexical.features.patchouli

import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.features.flora.ConjureFloraRecipe
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
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
		val recipes = world.recipeManager.listAllOfType(ConjureFloraRecipe.Type.INSTANCE).toMutableList()
		recipes.sortBy { it.cost }
		this.recipe = recipes.getOrNull(id)
	}

	override fun process(world: World, key: String): IVariable? {
		if (recipe == null)
			return null

		return when (key) {
			"icon" -> IVariable.from(ItemStack(HexItems.AMETHYST_DUST))
			"title" -> IVariable.from(recipe!!.state.block.name)
			"block" -> IVariable.from(ItemStack(recipe!!.state.block.asItem()))
			"cost" -> IVariable.from(costText(recipe!!.cost).setStyle(Style.EMPTY.withColor(ItemMediaHolder.HEX_COLOR)))
			"text" -> IVariable.from("hexical.page.conjure_flora.${Registries.BLOCK.getId(recipe!!.state.block)}".asTranslatedComponent)
			else -> null
		}
	}
}