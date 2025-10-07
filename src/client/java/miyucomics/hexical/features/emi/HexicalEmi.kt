package miyucomics.hexical.features.emi

import at.petrak.hexcasting.api.utils.putCompound
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.dyes.DyeingUtils
import miyucomics.hexical.features.media_jar.MediaJarBlock
import miyucomics.hexical.features.transmuting.TransmutingHelper
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound

class HexicalEmi : EmiPlugin {
	override fun register(registry: EmiRegistry) {
		registry.addCategory(DYEING_CATEGORY)
		registry.addCategory(TRANSMUTING_CATEGORY)

		registry.addWorkstation(TRANSMUTING_CATEGORY, TRANSMUTING_ICON)

		for (recipe in registry.recipeManager.listAllOfType(DyeingUtils.DYEING_BLOCK_RECIPE))
			registry.addRecipe(DyeingBlockEmi(recipe))
		for (recipe in registry.recipeManager.listAllOfType(DyeingUtils.DYEING_ITEM_RECIPE))
			registry.addRecipe(DyeingItemEmi(recipe))
		for (recipe in registry.recipeManager.listAllOfType(TransmutingHelper.TRANSMUTING_RECIPE))
			registry.addRecipe(TransmutingEmi(recipe))
	}

	companion object {
		val DYEING_CATEGORY = EmiRecipeCategory(HexicalMain.id("dyeing"), EmiStack.of(ItemStack(Items.RED_DYE)), EmiTexture(HexicalMain.id("textures/gui/dyeing_simplified.png"), 0, 0, 16, 16, 16, 16, 16, 16))

		val TRANSMUTING_ICON: EmiStack = EmiStack.of(ItemStack(HexicalBlocks.MEDIA_JAR_ITEM).also {
			val compound = NbtCompound()
			compound.putLong("media", MediaJarBlock.MAX_CAPACITY)
			it.orCreateNbt.putCompound("BlockEntityTag", compound)
		})
		val TRANSMUTING_CATEGORY = EmiRecipeCategory(HexicalMain.id("transmuting"), TRANSMUTING_ICON, EmiTexture(HexicalMain.id("textures/gui/transmuting_simplified.png"), 0, 0, 16, 16, 16, 16, 16, 16))
	}
}