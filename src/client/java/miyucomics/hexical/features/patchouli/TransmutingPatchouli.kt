package miyucomics.hexical.features.patchouli

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.features.transmuting.TransmutingRecipe
import net.minecraft.client.MinecraftClient
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
		val id = Identifier(vars["recipe"].asString())
		val recman = MinecraftClient.getInstance().world!!.recipeManager
		val transmutingRecipes = recman.listAllOfType(TransmutingRecipe.Type.INSTANCE)
		for (recipe in transmutingRecipes) {
			if (recipe.getId() == id) {
				this.recipe = recipe
				break
			}
		}
	}

	override fun process(world: World, key: String): IVariable? {
		if (key.length > 6 && key.take(6) == "output") {
			val index = Integer.parseInt(key.substring(6))
			if (index < TransmutingPatchouli.recipe.output.size)
				return IVariable.from(TransmutingPatchouli.recipe.output[index])
			return IVariable.from(ItemStack.EMPTY)
		}

		return when (key) {
			"input" -> IVariable.from(TransmutingPatchouli.recipe.input)
			"cost" -> IVariable.from(costText(TransmutingPatchouli.recipe.cost).setStyle(Style.EMPTY.withColor(ItemMediaHolder.HEX_COLOR)))
			else -> null
		}
	}
}

fun costText(media: Long): MutableText {
	val loss = media.toFloat() / MediaConstants.DUST_UNIT
	if (loss > 0f)
		return Text.translatable("hexical.recipe.transmute.media_cost", loss)
	if (loss < 0f)
		return Text.translatable("hexical.recipe.transmute.media_yield", -loss)
	return Text.translatable("hexical.recipe.transmute.media_free")
}