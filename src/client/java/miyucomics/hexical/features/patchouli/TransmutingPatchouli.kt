package miyucomics.hexical.features.patchouli

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.features.transmuting.TransmutingRecipe
import net.minecraft.item.ItemStack
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.world.World
import vazkii.patchouli.api.IComponentProcessor
import vazkii.patchouli.api.IVariable
import vazkii.patchouli.api.IVariableProvider

@Suppress("unused")
class TransmutingPatchouli : IComponentProcessor {
	lateinit var recipe: TransmutingRecipe

	override fun setup(world: World, vars: IVariableProvider) {
		this.recipe = world.recipeManager.listAllOfType(TransmutingRecipe.Type.INSTANCE).firstOrNull { it.id == Identifier(vars["recipe"].asString()) } ?: return
	}

	override fun process(world: World, key: String): IVariable? {
		if (key.length > 6 && key.take(6) == "output") {
			val index = Integer.parseInt(key.substring(6))
			if (index < this.recipe.output.size)
				return IVariable.from(this.recipe.output[index])
			return IVariable.from(ItemStack.EMPTY)
		}

		return when (key) {
			"input" -> IVariable.from(this.recipe.input)
			"cost" -> IVariable.from(costText(this.recipe.cost).setStyle(Style.EMPTY.withColor(ItemMediaHolder.HEX_COLOR)))
			else -> null
		}
	}
}

fun costText(media: Long): MutableText {
	val loss = media.toFloat() / MediaConstants.DUST_UNIT
	if (loss > 0f)
		return Text.translatable("hexical.recipe.media_cost", loss)
	if (loss < 0f)
		return Text.translatable("hexical.recipe.media_yield", -loss)
	return Text.translatable("hexical.recipe.media_free")
}