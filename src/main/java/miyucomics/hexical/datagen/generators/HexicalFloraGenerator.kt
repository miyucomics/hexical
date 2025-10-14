package miyucomics.hexical.datagen.generators

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.JsonObject
import miyucomics.hexical.inits.HexicalBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.block.Blocks
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.registry.Registries
import java.util.concurrent.CompletableFuture

class HexicalFloraGenerator(val output: FabricDataOutput) : DataProvider {
	override fun getName() = "Hexical Flora"
	override fun run(writer: DataWriter): CompletableFuture<*> {
		val recipePath = output.getResolver(DataOutput.OutputType.DATA_PACK, "recipes/flora/")
		return CompletableFuture.allOf(*defaultRecipes.map { (flower, cost) ->
			val identifier = Registries.BLOCK.getId(flower)
			DataProvider.writeToPath(writer, JsonObject().apply {
				addProperty("type", "hexical:conjure_flora")
				addProperty("cost", cost)
				add("block", StateIngredientHelper.serializeBlockState(flower.defaultState))
			}, recipePath.resolve(identifier, "json"))
		}.toTypedArray())
	}

	companion object {
		val defaultRecipes = mapOf(
			Blocks.PINK_PETALS to 2500,

			Blocks.GRASS to 3000,
			Blocks.TALL_GRASS to 3000,

			Blocks.FERN to 3500,
			Blocks.LARGE_FERN to 3500,

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

			Blocks.RED_MUSHROOM to 6000,
			Blocks.BROWN_MUSHROOM to 6000,
			Blocks.CRIMSON_FUNGUS to 6000,
			Blocks.WARPED_FUNGUS to 6000,
			Blocks.CRIMSON_ROOTS to 6000,
			Blocks.WARPED_ROOTS to 6000,

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

			Blocks.WITHER_ROSE to 100000,
			Blocks.CACTUS to 100000
		)
	}
}