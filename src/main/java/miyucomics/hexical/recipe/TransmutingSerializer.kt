package miyucomics.hexical.recipe

import com.google.gson.*
import com.mojang.serialization.JsonOps
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class TransmutingSerializer() : RecipeSerializer<TransmutingRecipe> {
	override fun read(recipeId: Identifier, json: JsonObject): TransmutingRecipe {
		val recipeJson: TransmutingFormat = Gson().fromJson(json, TransmutingFormat::class.java)
		if (recipeJson.input == null || recipeJson.output == null)
			throw JsonSyntaxException("Input or output is missing in recipe $recipeId")

		val outputs = when (val output = recipeJson.output!!) {
			is JsonArray -> output.map { deriveSingleItem(it, recipeId) }
			else -> listOf(deriveSingleItem(output, recipeId))
		}

		return TransmutingRecipe(recipeId, Ingredient.fromJson(recipeJson.input), recipeJson.count, recipeJson.cost, outputs)
	}

	override fun write(buf: PacketByteBuf, recipe: TransmutingRecipe) {
		recipe.input.write(buf)
		buf.writeInt(recipe.inputCount)
		buf.writeLong(recipe.cost)
		buf.writeInt(recipe.output.size)
		for (item in recipe.output)
			buf.writeItemStack(item)
	}

	override fun read(id: Identifier, buf: PacketByteBuf): TransmutingRecipe {
		val input = Ingredient.fromPacket(buf)
		val inputCount = buf.readInt()
		val mediaCost = buf.readLong()

		val outputs = mutableListOf<ItemStack>()
		val length = buf.readInt()
		for (i in 0 until length)
			outputs.add(buf.readItemStack())

		return TransmutingRecipe(id, input, inputCount, mediaCost, outputs)
	}

	companion object {
		val INSTANCE: TransmutingSerializer = TransmutingSerializer()

		fun deriveSingleItem(thing: JsonElement, recipeId: Identifier): ItemStack {
			return when (thing) {
				is JsonObject -> deriveComplexItem(thing, recipeId)
				is JsonPrimitive -> ItemStack(Registries.ITEM.getOrEmpty(Identifier(thing.asString)).orElseThrow { JsonSyntaxException("No such item $thing") })
				else -> throw IllegalStateException("$thing is not a valid single item stack format.")
			}
		}

		private fun deriveComplexItem(output: JsonObject, recipeId: Identifier): ItemStack {
			var outputCount = 1
			var outputNBT: NbtElement? = null

			val outputItemID: String = output.get("item").asString
			val outputItem = Registries.ITEM.getOrEmpty(Identifier(outputItemID)).orElseThrow { JsonSyntaxException("No such item $outputItemID") }

			if (output.has("count"))
				outputCount = output.get("count").asInt
			if (output.has("nbt"))
				outputNBT = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, output.get("nbt"))

			val outputStack = ItemStack(outputItem, outputCount)
			if (outputNBT != null) {
				if (outputNBT is NbtCompound)
					outputStack.nbt = outputNBT
				else
					throw IllegalStateException("Weird NBT: $outputItemID in recipe $recipeId")
			}

			return outputStack
		}
	}
}

class TransmutingFormat {
	var input: JsonObject? = null
	var output: JsonElement? = null
	var count: Int = 0
	var cost: Long = 0
}