package miyucomics.hexical.features.integrations

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.dyes.block.DyeingBlockRecipe
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier

class DyeingEmi(recipe: DyeingBlockRecipe) : BasicEmiRecipe(HexicalEmi.DYEING_CATEGORY, recipe.getId(), 134, 52) {
	init {
		this.inputs.add(EmiIngredient.of(Ingredient.ofStacks(recipe.inputs.flatMap { it.displayedStacks }.stream())))
		this.outputs.add(EmiStack.of(recipe.output.block.asItem()))
	}

	override fun addWidgets(widgets: WidgetHolder) {
		widgets.addTexture(OVERLAY, 0, 0, 134, 40, 0, 40, 134, 40, 256, 128)
		widgets.addSlot(inputs[0], 12, 12)
		widgets.addSlot(outputs[0], 42, 12)
	}

	companion object {
		private val OVERLAY: Identifier = HexicalMain.id("textures/gui/dyeing_emi.png")
	}
}