package miyucomics.hexical.features.integrations

import at.petrak.hexcasting.api.utils.putCompound
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.media_jar.MediaJarBlock
import miyucomics.hexical.features.transmuting.TransmutingHelper
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class HexicalEmi : EmiPlugin {
	override fun register(registry: EmiRegistry) {
		registry.addCategory(TRANSMUTING_CATEGORY)
		registry.addWorkstation(TRANSMUTING_CATEGORY, ICON)
		for (recipe in registry.recipeManager.listAllOfType(TransmutingHelper.TRANSMUTING_RECIPE))
			registry.addRecipe(TransmutingEmi(recipe))
	}

	companion object {
		val ICON: EmiStack = EmiStack.of(ItemStack(HexicalBlocks.MEDIA_JAR_ITEM).also {
			val compound = NbtCompound()
			compound.putLong("media", MediaJarBlock.MAX_CAPACITY)
			it.orCreateNbt.putCompound("BlockEntityTag", compound)
		})
		val TRANSMUTING_CATEGORY: EmiRecipeCategory = EmiRecipeCategory(HexicalMain.id("transmuting"), ICON, EmiTexture(HexicalMain.id("textures/gui/transmuting_simplified.png"), 0, 0, 16, 16, 16, 16, 16, 16))
	}
}