package miyucomics.hexical.datagen.providers

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.JsonObject
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.flora.ConjureFloraSerializer
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

object FloraProvider {
	val recipeJsons = mutableListOf<FloraJsonGenerator>()
	val recipePages = mutableListOf<JsonObject>()

	fun init() {
		defaultRecipes.forEach { (flower, cost) ->
			val name = Registries.BLOCK.getId(flower).path
			recipeJsons.add(FloraJsonGenerator(HexicalMain.id("flora/$name"), flower, cost))
			recipePages.add(JsonObject().apply {
				addProperty("type", "hexcasting:flora")
				addProperty("recipe", "hexical:flora/$name")
				addProperty("title", flower.translationKey)
				addProperty("text", "hexical.page.flora.$name")
			})
		}
	}

	val defaultRecipes: Map<Block, Long> = mapOf(
		Blocks.PINK_PETALS to 2500,

		Blocks.GRASS to 3000,
		Blocks.TALL_GRASS to 3000,

		Blocks.FERN to 3500,
		Blocks.LARGE_FERN to 3500,
		Blocks.NETHER_SPROUTS to 3500,
		Blocks.SEAGRASS to 3500,

		Blocks.DANDELION to 5000,
		Blocks.POPPY to 5000,
		Blocks.BLUE_ORCHID to 5000,
		Blocks.ALLIUM to 5000,
		Blocks.AZURE_BLUET to 5000,
		Blocks.RED_TULIP to 5000,
		Blocks.PINK_TULIP to 5000,
		Blocks.ORANGE_TULIP to 5000,
		Blocks.WHITE_TULIP to 5000,
		Blocks.OXEYE_DAISY to 5000,
		Blocks.CORNFLOWER to 5000,
		Blocks.LILY_OF_THE_VALLEY to 5000,
		Blocks.SUNFLOWER to 5000,
		Blocks.ROSE_BUSH to 5000,
		Blocks.PEONY to 5000,
		Blocks.LILAC to 5000,
		Blocks.KELP to 5000,

		Blocks.RED_MUSHROOM to 6000,
		Blocks.BROWN_MUSHROOM to 6000,
		Blocks.CRIMSON_FUNGUS to 6000,
		Blocks.WARPED_FUNGUS to 6000,
		Blocks.CRIMSON_ROOTS to 6000,
		Blocks.WARPED_ROOTS to 6000,
		Blocks.SEA_PICKLE to 6000,

		Blocks.SMALL_DRIPLEAF to 7000,
		Blocks.BIG_DRIPLEAF to 7000,

		Blocks.DEAD_BUSH to 7500,
		Blocks.NETHER_WART to 7500,

		HexicalBlocks.PERIWINKLE_FLOWER to 10000,
		Blocks.TORCHFLOWER to 15000,
		Blocks.PITCHER_PLANT to 15000,

		Blocks.ACACIA_SAPLING to 50000,
		Blocks.BAMBOO to 50000,
		Blocks.BIRCH_SAPLING to 50000,
		Blocks.CHERRY_SAPLING to 50000,
		Blocks.DARK_OAK_SAPLING to 50000,
		Blocks.JUNGLE_SAPLING to 50000,
		Blocks.OAK_SAPLING to 50000,
		Blocks.SPRUCE_SAPLING to 50000,
		Blocks.MANGROVE_PROPAGULE to 50000,
		Blocks.SUGAR_CANE to 50000,

		Blocks.WITHER_ROSE to 100000,
		Blocks.CACTUS to 100000
	)
}

class FloraJsonGenerator(private val id: Identifier, private val flower: Block, private val cost: Long) : RecipeJsonProvider {
	override fun serialize(json: JsonObject) {
		json.addProperty("cost", cost)
		json.add("block", StateIngredientHelper.serializeBlockState(flower.defaultState))
	}

	override fun getRecipeId(): Identifier = id
	override fun getSerializer(): RecipeSerializer<*> = ConjureFloraSerializer.INSTANCE
	override fun toAdvancementJson(): JsonObject? = null
	override fun getAdvancementId(): Identifier? = null
}