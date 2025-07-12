package miyucomics.hexical.recipe

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtOps
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class TransmutingJsonProvider(private val id: Identifier, private val input: Ingredient, private val output: List<ItemStack>, private val cost: Long) : RecipeJsonProvider {
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