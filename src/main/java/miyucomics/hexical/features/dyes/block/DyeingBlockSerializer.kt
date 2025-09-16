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
		return DyeingBlockRecipe(recipeId, enumValues<DyeOption>()[raw.dye], raw.inputs.map(StateIngredientHelper::deserialize), StateIngredientHelper.readBlockState(raw.output))
	}

	override fun write(buf: PacketByteBuf, recipe: DyeingBlockRecipe) {
		buf.writeInt(recipe.dye.ordinal)
		buf.writeInt(recipe.inputs.size)
		recipe.inputs.forEach { it.write(buf) }
		buf.writeVarInt(Block.getRawIdFromState(recipe.output))
	}

	override fun read(id: Identifier, buf: PacketByteBuf): DyeingBlockRecipe {
		val color = enumValues<DyeOption>()[buf.readInt()]
		val inputs = (0..buf.readInt()).map { StateIngredientHelper.read(buf) }
		val output = Block.getStateFromRawId(buf.readVarInt())
		return DyeingBlockRecipe(id, color, inputs, output)
	}

	companion object {
		val INSTANCE: DyeingBlockSerializer = DyeingBlockSerializer()
	}
}

private class DataFormat {
	val dye: Int = 0
	val inputs: List<JsonObject>? = null
	val output: JsonObject? = null
}