package miyucomics.hexical.datagen

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.lib.HexItems
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.transmuting.TransmutingSerializer
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtOps
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

object TransmutationProvider {
	val transmutationRecipeJsons = mutableListOf<TransmutingJsonGenerator>()
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
		transmutationRecipeJsons.add(TransmutingJsonGenerator(HexicalMain.id("transmuting/$name"), Ingredient.ofItems(original), listOf(ItemStack(new)), cost))
		transmutationRecipePages.add(JsonObject().apply {
			addProperty("type", "hexcasting:transmuting")
			addProperty("recipe", "hexical:transmuting/$name")
			addProperty("title", "hexical.recipe.$name.header")
			addProperty("text", "hexical.recipe.$name.text")
		})
	}
}

class TransmutingJsonGenerator(private val id: Identifier, private val input: Ingredient, private val output: List<ItemStack>, private val cost: Long) : RecipeJsonProvider {
	override fun serialize(json: JsonObject) {
		json.add("input", input.toJson())
		json.addProperty("cost", cost)

		if (output.size == 1) {
			json.add("output", serializeItemStack(output[0]))
		} else {
			val array = JsonArray()
			output.forEach { array.add(serializeItemStack(it)) }
			json.add("output", array)
		}
	}

	override fun getRecipeId(): Identifier = id
	override fun getSerializer(): RecipeSerializer<*> = TransmutingSerializer.INSTANCE
	override fun toAdvancementJson(): JsonObject? = null
	override fun getAdvancementId(): Identifier? = null

	private fun serializeItemStack(stack: ItemStack): JsonObject {
		val obj = JsonObject()
		obj.addProperty("item", Registries.ITEM.getId(stack.item).toString())
		if (stack.count > 1) obj.addProperty("count", stack.count)
		if (stack.hasNbt()) obj.add("nbt", NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, stack.nbt))
		return obj
	}
}