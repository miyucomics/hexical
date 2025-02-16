package miyucomics.hexical.recipe

import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
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
		if (key.length > 6 && key.substring(0, 6) == "output") {
			val index = Integer.parseInt(key.substring(6))
			if (index < recipe.output.size)
				return IVariable.from(recipe.output[index])
			return IVariable.from(ItemStack.EMPTY)
		}

		return when (key) {
			"input" -> IVariable.from(recipe.input)
			"cost" -> IVariable.from(costText(recipe.cost).setStyle(Style.EMPTY.withColor(ItemMediaHolder.HEX_COLOR)))
			else -> null
		}
	}
}

fun costText(media: Long): MutableText {
	val loss = media.toFloat() / 10000f
	if (loss > 0f)
		return Text.translatable("hexical.recipe.transmute.media_cost", loss)
	if (loss < 0f)
		return Text.translatable("hexical.recipe.transmute.media_yield", -loss)
	return Text.translatable("hexical.recipe.transmute.media_free")
}