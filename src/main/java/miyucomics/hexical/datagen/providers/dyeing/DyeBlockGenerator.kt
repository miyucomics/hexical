package miyucomics.hexical.datagen.providers.dyeing

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredient
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import miyucomics.hexical.features.dyes.DyeOption
import miyucomics.hexical.features.dyes.block.DyeingBlockSerializer
import net.minecraft.block.Block
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class DyeBlockGenerator(private val id: Identifier, val dye: DyeOption, val inputs: List<StateIngredient>, val output: Block) : DyeRecipeJsonGenerator {
	override fun serialize(json: JsonObject) {
		json.addProperty("dye", dye.ordinal)
		json.add("input", JsonArray().apply {
			inputs.forEach { add(it.serialize()) }
		})
		json.addProperty("output", Registries.BLOCK.getId(output).toString())
	}

	override fun getRecipeId(): Identifier = id
	override fun getSerializer(): RecipeSerializer<*> = DyeingBlockSerializer.INSTANCE
	override fun toAdvancementJson(): JsonObject? = null
	override fun getAdvancementId(): Identifier? = null
}