package miyucomics.hexical.features.integrations

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.transmuting.TransmutingRecipe
import net.minecraft.util.Identifier

class TransmutingEmi(val recipe: TransmutingRecipe) : BasicEmiRecipe(HexicalEmi.TRANSMUTING_CATEGORY, recipe.getId(), 134, 52) {
	init {
		this.inputs.add(EmiIngredient.of(recipe.input))
		this.outputs.addAll(recipe.output.map(EmiStack::of))
	}

	override fun addWidgets(widgets: WidgetHolder) {
		widgets.addTexture(OVERLAY, 0, 0, 134, 40, 0, 40 * (this.outputs.size - 1), 134, 40, 256, 128)
		widgets.addSlot(inputs[0], 12, 12)
		this.outputs.forEachIndexed { index, stack -> widgets.addSlot(stack, 50 + 28 * index, 12).recipeContext(this) }
		widgets.addText(costText(recipe.cost), 10, 37, -1, true)
	}

	companion object {
		private val OVERLAY: Identifier = HexicalMain.id("textures/gui/transmuting_emi.png")
	}
}