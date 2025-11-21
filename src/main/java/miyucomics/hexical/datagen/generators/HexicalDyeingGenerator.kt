package miyucomics.hexical.datagen.generators

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import miyucomics.hexical.features.dyes.DyeOption
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class HexicalDyeingGenerator(val output: FabricDataOutput) : DataProvider {
	override fun getName() = "Hexical Dyeing"
	override fun run(writer: DataWriter): CompletableFuture<*> = CompletableFuture.allOf(
		*generateBlocks(writer).toTypedArray(),
		*generateItems(writer).toTypedArray()
	)

	private fun generateBlocks(writer: DataWriter): List<CompletableFuture<*>> {
		val tasks = mutableListOf<CompletableFuture<*>>()
		val recipePath = output.getResolver(DataOutput.OutputType.DATA_PACK, "recipes/dyeing/blocks")
		val dyeLookup = JsonObject()

		blockPatterns.forEach { (groupName, pattern) ->
			val groupName = Identifier(groupName)
			val blocks = DyeOption.entries.mapNotNull { dye -> resolvePattern(Registries.BLOCK, pattern, dye)?.let { dye to it } }.toMap()
			blocks.keys.forEach { dye ->
				val blockId = Registries.BLOCK.getId(blocks[dye]).toString()
				dyeLookup.addProperty(blockId, dye.ordinal)
				tasks.add(DataProvider.writeToPath(writer, JsonObject().apply {
					addProperty("type", "hexical:dye_block")
					addProperty("dye", dye.ordinal)
					addProperty("group", groupName.toString())
					add("inputs", JsonArray().apply {
						blocks.values.forEach { block ->
							add(JsonObject().apply {
								addProperty("type", "block")
								addProperty("block", Registries.BLOCK.getId(block).toString())
							})
						}
					})
					add("output", JsonObject().apply { addProperty("name", blockId) })
				}, recipePath.resolve(Identifier(groupName.namespace, "${groupName.path}_${dye.replacement}"), "json")))
			}
		}

		tasks.add(DataProvider.writeToPath(writer, dyeLookup, output.getResolver(DataOutput.OutputType.DATA_PACK, "dyeing/blocks").resolve(Identifier("hexical", "default"), "json")))

		return tasks
	}

	private fun generateItems(writer: DataWriter): List<CompletableFuture<*>> {
		val tasks = mutableListOf<CompletableFuture<*>>()
		val recipePath = output.getResolver(DataOutput.OutputType.DATA_PACK, "recipes/dyeing/items")
		val dyeLookup = JsonObject()

		itemPatterns.forEach { (groupName, pattern) ->
			val groupName = Identifier(groupName)
			val items = DyeOption.entries.mapNotNull { dye -> resolvePattern(Registries.ITEM, pattern, dye)?.let { dye to it } }.toMap()
			items.keys.forEach { dye ->
				val itemId = Registries.ITEM.getId(items[dye]).toString()
				dyeLookup.addProperty(itemId, dye.ordinal)
				tasks.add(DataProvider.writeToPath(writer, JsonObject().apply {
					addProperty("type", "hexical:dye_item")
					addProperty("dye", dye.ordinal)
					addProperty("group", groupName.toString())
					add("inputs", JsonArray().apply { items.values.forEach { add(JsonObject().apply {
						addProperty("item", Registries.ITEM.getId(it).toString())
					}) } })
					addProperty("output", itemId)
				}, recipePath.resolve(Identifier(groupName.namespace, "${groupName.path}_${dye.replacement}"), "json")))
			}
		}

		tasks.add(DataProvider.writeToPath(writer, dyeLookup, output.getResolver(DataOutput.OutputType.DATA_PACK, "dyeing/items").resolve(Identifier("hexical", "default"), "json")))

		return tasks
	}

	companion object {
		val blockPatterns = mapOf(
			"minecraft:bed" to "minecraft:{color}bed",
			"minecraft:candle" to "minecraft:{color}candle",
			"minecraft:candle_cake" to "minecraft:{color}candle_cake",
			"minecraft:carpet" to "minecraft:{color}carpet",
			"minecraft:concrete" to "minecraft:{color}concrete",
			"minecraft:concrete_powder" to "minecraft:{color}concrete_powder",
			"minecraft:glazed_terracotta" to "minecraft:{color}glazed_terracotta",
			"minecraft:sand" to "minecraft:{color}sand",
			"minecraft:sandstone" to "minecraft:{color}sandstone",
			"minecraft:cut_sandstone" to "minecraft:cut_{color}sandstone",
			"minecraft:smooth_sandstone" to "minecraft:smooth_{color}sandstone",
			"minecraft:chiseled_sandstone" to "minecraft:chiseled_{color}sandstone",
			"minecraft:sandstone_slab" to "minecraft:{color}sandstone_slab",
			"minecraft:cut_sandstone_slab" to "minecraft:cut_{color}sandstone_slab",
			"minecraft:smooth_sandstone_slab" to "minecraft:smooth_{color}sandstone_slab",
			"minecraft:sandstone_stairs" to "minecraft:{color}sandstone_stairs",
			"minecraft:smooth_sandstone_stairs" to "minecraft:smooth_{color}sandstone_stairs",
			"minecraft:sandstone_walls" to "minecraft:{color}sandstone_walls",
			"minecraft:shulker_box" to "minecraft:{color}shulker_box",
			"minecraft:stained_glass" to "minecraft:{color}stained_glass",
			"minecraft:stained_glass_pane" to "minecraft:{color}stained_glass_pane",
			"minecraft:terracotta" to "minecraft:{color}terracotta",
			"minecraft:tulip" to "minecraft:{color}tulip",
			"minecraft:wool" to "minecraft:{color}wool"
		)

		val itemPatterns = mapOf(
			"minecraft:dye" to "minecraft:{color}dye",
			"hexcasting:pigment" to "hexcasting:dye_colorizer_{color}"
		)

		private fun <T : Any> resolvePattern(registry: Registry<T>, pattern: String, dye: DyeOption) = listOfNotNull(
			registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement)))),
			registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement + "_"))))
		).firstOrNull()
	}
}