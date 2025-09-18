package miyucomics.hexical.features.dyes.item

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import miyucomics.hexical.features.dyes.DyeOption
import miyucomics.hexical.features.transmuting.TransmutingSerializer
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

class DyeingItemSerializer : RecipeSerializer<DyeingItemRecipe> {
	override fun read(recipeId: Identifier, json: JsonObject): DyeingItemRecipe {
		val raw: DataFormat = Gson().fromJson(json, DataFormat::class.java)
		if (raw.inputs == null)
			throw JsonSyntaxException("Possible inputs are missing in recipe $recipeId")
		if (raw.output == null)
			throw JsonSyntaxException("Output is missing in recipe $recipeId")
		return DyeingItemRecipe(recipeId, enumValues<DyeOption>()[raw.dye], Ingredient.fromJson(raw.inputs), TransmutingSerializer.deriveSingleItem(raw.output, recipeId))
	}

	override fun write(buf: PacketByteBuf, recipe: DyeingItemRecipe) {
		buf.writeInt(recipe.dye.ordinal)
		recipe.inputs.write(buf)
		buf.writeItemStack(recipe.output)
	}

	override fun read(id: Identifier, buf: PacketByteBuf): DyeingItemRecipe {
		val color = enumValues<DyeOption>()[buf.readInt()]
		return DyeingItemRecipe(id, color, Ingredient.fromPacket(buf), buf.readItemStack())
	}

	companion object {
		val INSTANCE: DyeingItemSerializer = DyeingItemSerializer()
	}
}

private class DataFormat {
	val dye: Int = 0
	val inputs: JsonElement? = null
	val output: JsonElement? = null
}