package miyucomics.hexical.features.flora

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.block.Block
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

class ConjureFloraSerializer : RecipeSerializer<ConjureFloraRecipe> {
	override fun read(recipeId: Identifier, json: JsonObject): ConjureFloraRecipe {
		val raw: DataFormat = Gson().fromJson(json, DataFormat::class.java)
		if (raw.block == null)
			throw JsonSyntaxException("Output is missing in recipe $recipeId")
		return ConjureFloraRecipe(recipeId, StateIngredientHelper.readBlockState(raw.block), raw.cost)
	}

	override fun write(buf: PacketByteBuf, recipe: ConjureFloraRecipe) {
		buf.writeVarInt(Block.getRawIdFromState(recipe.block))
		buf.writeLong(recipe.cost)
	}

	override fun read(id: Identifier, buf: PacketByteBuf) = ConjureFloraRecipe(id, Block.getStateFromRawId(buf.readVarInt()), buf.readLong())

	companion object {
		val INSTANCE: ConjureFloraSerializer = ConjureFloraSerializer()
	}
}

private class DataFormat {
	val block: JsonObject? = null
	val cost: Long = 0
}