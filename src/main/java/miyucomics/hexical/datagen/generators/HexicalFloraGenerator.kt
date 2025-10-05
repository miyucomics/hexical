package miyucomics.hexical.datagen.generators

import com.google.gson.JsonObject
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class HexicalFloraGenerator(val output: FabricDataOutput) : DataProvider {
	override fun getName() = "Hexical Flora"
	override fun run(writer: DataWriter): CompletableFuture<*> {
		val recipePath = output.getResolver(DataOutput.OutputType.DATA_PACK, "recipes/flora/")
		return CompletableFuture.allOf(*defaultRecipes.map { (flower, cost) ->
			DataProvider.writeToPath(writer, JsonObject().apply {
				addProperty("type", "hexical:conjure_flora")
				addProperty("block", flower)
				addProperty("cost", cost)
			}, recipePath.resolve(Identifier(flower), "json"))
		}.toTypedArray())
	}

	companion object {
		val defaultRecipes = mapOf(
			"minecraft:pink_petals" to 2500,
			"minecraft:tall_grass" to 3000,
			"minecraft:grass" to 3000,
			"minecraft:dandelion" to 5000,
			"minecraft:poppy" to 5000,
			"minecraft:blue_orchid" to 5000,
			"minecraft:allium" to 5000,
			"minecraft:azure_bluet" to 5000,
			"minecraft:red_tulip" to 5000,
			"minecraft:orange_tulip" to 5000,
			"minecraft:white_tulip" to 5000,
			"minecraft:pink_tulip" to 5000,
			"minecraft:oxeye_daisy" to 5000,
			"minecraft:cornflower" to 5000,
			"minecraft:lily_of_the_valley" to 5000,
			"minecraft:sunflower" to 5000,
			"minecraft:rose_bush" to 5000,
			"minecraft:fern" to 5500,
			"minecraft:red_mushroom" to 6000,
			"minecraft:brown_mushroom" to 6000,
			"minecraft:crimson_fungus" to 6000,
			"minecraft:warped_fungus" to 6000,
			"minecraft:crimson_roots" to 6000,
			"minecraft:warped_roots" to 6000,
			"minecraft:dead_bush" to 7500,
			"minecraft:nether_wart" to 7500,
			"hexical:periwinkle" to 10000,
			"minecraft:torchflower" to 15000,
			"minecraft:pitcher_plant" to 15000,
			"minecraft:wither_rose" to 100000
		)
	}
}