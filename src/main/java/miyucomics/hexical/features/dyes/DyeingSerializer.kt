package miyucomics.hexical.features.dyes

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.block.Block
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

class DyeingSerializer : RecipeSerializer<DyeingBlockRecipe> {
	override fun read(recipeId: Identifier, json: JsonObject): DyeingBlockRecipe {
		val raw: DyeingFormat = Gson().fromJson(json, DyeingFormat::class.java)
		if (raw.input == null)
			throw JsonSyntaxException("Input is missing in recipe $recipeId")
		if (raw.output == null)
			throw JsonSyntaxException("Output is missing in recipe $recipeId")

		return DyeingBlockRecipe(recipeId, enumValues<DyeOption>()[raw.dye], StateIngredientHelper.deserialize(raw.input), StateIngredientHelper.readBlockState(raw.output))
	}

	override fun write(buf: PacketByteBuf, recipe: DyeingBlockRecipe) {
		buf.writeInt(recipe.dye.ordinal)
		recipe.input.write(buf)
		buf.writeVarInt(Block.getRawIdFromState(recipe.output))
	}

	override fun read(id: Identifier, buf: PacketByteBuf): DyeingBlockRecipe = DyeingBlockRecipe(id, enumValues<DyeOption>()[buf.readInt()], StateIngredientHelper.read(buf), Block.getStateFromRawId(buf.readVarInt()))

	companion object {
		val INSTANCE: DyeingSerializer = DyeingSerializer()
	}
}

class DyeingFormat {
	var dye: Int = 0
	var input: JsonObject? = null
	var output: JsonObject? = null
}