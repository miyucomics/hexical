package miyucomics.hexical.features.flora

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class ConjureFloraSerializer : RecipeSerializer<ConjureFloraRecipe> {
	override fun read(recipeId: Identifier, json: JsonObject): ConjureFloraRecipe {
		val raw: DataFormat = Gson().fromJson(json, DataFormat::class.java)
		return ConjureFloraRecipe(recipeId, Registries.BLOCK.get(Identifier(raw.block)), raw.cost)
	}

	override fun write(buf: PacketByteBuf, recipe: ConjureFloraRecipe) {
		buf.writeIdentifier(Registries.BLOCK.getId(recipe.block))
		buf.writeLong(recipe.cost)
	}

	override fun read(id: Identifier, buf: PacketByteBuf) = ConjureFloraRecipe(id, Registries.BLOCK.get(buf.readIdentifier()), buf.readLong())

	companion object {
		val INSTANCE: ConjureFloraSerializer = ConjureFloraSerializer()
	}
}

private class DataFormat {
	val block: String = ""
	val cost: Long = 0
}