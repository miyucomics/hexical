package miyucomics.hexical.features.patchouli

import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.features.flora.ConjureFloraRecipe
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.util.Identifier
import net.minecraft.world.World
import vazkii.patchouli.api.IComponentProcessor
import vazkii.patchouli.api.IVariable
import vazkii.patchouli.api.IVariableProvider

@Suppress("unused")
class ConjureFloraPatchouli : IComponentProcessor {
	lateinit var recipe: ConjureFloraRecipe

	override fun setup(world: World, vars: IVariableProvider) {
		this.recipe = world.recipeManager.listAllOfType(ConjureFloraRecipe.Type.INSTANCE).firstOrNull { it.id == Identifier(vars["recipe"].asString()) } ?: return
	}

	override fun process(world: World, key: String): IVariable? {
		return when (key) {
			"block" -> IVariable.from(ItemStack(recipe.state.block.asItem()))
			"cost" -> IVariable.from(costText(recipe.cost).setStyle(Style.EMPTY.withColor(ItemMediaHolder.HEX_COLOR)))
			else -> null
		}
	}
}