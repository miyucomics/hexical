package miyucomics.hexical.features.dyes.block

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import miyucomics.hexical.features.dyes.DyeOption
import net.minecraft.block.Block
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

class DyeingBlockSerializer : RecipeSerializer<DyeingBlockRecipe> {
	override fun read(recipeId: Identifier, json: JsonObject): DyeingBlockRecipe {
		val raw: DataFormat = Gson().fromJson(json, DataFormat::class.java)
		if (raw.inputs == null)
			throw JsonSyntaxException("Possible inputs are missing in recipe $recipeId")
		if (raw.output == null)
			throw JsonSyntaxException("Output is missing in recipe $recipeId")
		return DyeingBlockRecipe(recipeId, Identifier(raw.group), enumValues<DyeOption>()[raw.dye], raw.inputs.map(StateIngredientHelper::deserialize), StateIngredientHelper.readBlockState(raw.output))
	}

	override fun write(buf: PacketByteBuf, recipe: DyeingBlockRecipe) {
		buf.writeIdentifier(recipe.group)
		buf.writeInt(recipe.dye.ordinal)
		buf.writeInt(recipe.inputs.size)
		recipe.inputs.forEach { it.write(buf) }
		buf.writeVarInt(Block.getRawIdFromState(recipe.output))
	}

	override fun read(id: Identifier, buf: PacketByteBuf) = DyeingBlockRecipe(
        id,
        buf.readIdentifier(),
        enumValues<DyeOption>()[buf.readInt()],
        (0 until buf.readInt()).map{
            StateIngredientHelper.read(buf)
        },
        Block.getStateFromRawId(buf.readVarInt())
    )

	companion object {
		val INSTANCE: DyeingBlockSerializer = DyeingBlockSerializer()
	}
}

private class DataFormat {
	val group: String = ""
	val dye: Int = 0
	val inputs: List<JsonObject>? = null
	val output: JsonObject? = null
}