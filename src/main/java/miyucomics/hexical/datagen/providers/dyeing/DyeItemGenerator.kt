package miyucomics.hexical.datagen.providers.dyeing

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import miyucomics.hexical.features.dyes.DyeOption
import miyucomics.hexical.features.dyes.item.DyeingItemSerializer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtOps
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class DyeItemGenerator(private val id: Identifier, val dye: DyeOption, val input: Ingredient, val output: ItemStack) : DyeRecipeJsonGenerator {
	override fun serialize(json: JsonObject) {
		json.addProperty("dye", dye.ordinal)
		json.add("input", input.toJson())
		json.add("output", serializeItemStack(output))
	}

	override fun getRecipeId(): Identifier = id
	override fun getSerializer(): RecipeSerializer<*> = DyeingItemSerializer.INSTANCE
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