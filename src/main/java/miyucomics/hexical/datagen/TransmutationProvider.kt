package miyucomics.hexical.datagen

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.lib.HexItems
import com.google.gson.JsonObject
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.transmuting.TransmutingJsonProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient

object TransmutationProvider {
	val transmutationRecipeJsons = mutableListOf<TransmutingJsonProvider>()
	val transmutationRecipePages = mutableListOf<JsonObject>()

	fun init() {
		makeTransmutation("alchemists_take_this", Items.COPPER_INGOT, Items.GOLD_INGOT, MediaConstants.SHARD_UNIT)
		makeTransmutation("cry_obsidian", Items.OBSIDIAN, Items.CRYING_OBSIDIAN, MediaConstants.SHARD_UNIT)
		makeTransmutation("uncry_obsidian", Items.CRYING_OBSIDIAN, Items.OBSIDIAN, -2 * MediaConstants.DUST_UNIT)
		makeTransmutation("thoughtknot", Items.STRING, HexItems.THOUGHT_KNOT, (0.75 * MediaConstants.DUST_UNIT).toLong())
		makeTransmutation("unthoughtknot", HexItems.THOUGHT_KNOT, Items.STRING, MediaConstants.DUST_UNIT / -2)
		makeTransmutation("fossil_fuel", Items.CHARCOAL, Items.COAL, MediaConstants.DUST_UNIT / 2)
		makeTransmutation("renewable_fuel", Items.COAL, Items.CHARCOAL, MediaConstants.DUST_UNIT / -4)
	}

	fun makeTransmutation(name: String, original: Item, new: Item, cost: Long) {
		transmutationRecipeJsons.add(TransmutingJsonProvider(HexicalMain.id("transmuting/$name"), Ingredient.ofItems(original), listOf(ItemStack(new)), cost))
		transmutationRecipePages.add(JsonObject().apply {
			addProperty("type", "hexcasting:transmuting")
			addProperty("recipe", "hexical:transmuting/$name")
			addProperty("title", "hexical.recipe.$name.header")
			addProperty("text", "hexical.recipe.$name.text")
		})
	}
}