package miyucomics.hexical.datagen.providers

import com.google.gson.JsonObject
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.dyes.DyeOption
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

object DyeingProvider {
	fun init() {
		blockPatterns.forEach { (group, pattern) ->
			val json = JsonObject().apply {
				val collection = DyeOption.entries.mapNotNull { resolve(Registries.BLOCK, pattern, it) }.associate { (dye, identifier) ->
					addProperty(dye.toString(), identifier)
					DyeOption.entries[dye] to identifier
				}
				generatePage(group, collection)
			}
			generateFiles(json, "dyeing/block/", group)
		}

		itemPatterns.forEach { (group, pattern) ->
			val json = JsonObject().apply {
				val collection = DyeOption.entries.mapNotNull { resolve(Registries.ITEM, pattern, it) }.associate { (dye, identifier) ->
					addProperty(dye.toString(), identifier)
					DyeOption.entries[dye] to identifier
				}
				generatePage(group, collection)
			}
			generateFiles(json, "dyeing/item/", group)
		}
	}

	val blockPatterns = mapOf(
		"bed" to "minecraft:{color}bed",
		"candle" to "minecraft:{color}candle",
		// "candle_cake" to "minecraft:{color}candle_cake",
		"carpet" to "minecraft:{color}carpet",
		"concrete" to "minecraft:{color}concrete",
		"concrete_powder" to "minecraft:{color}concrete_powder",
		"glazed_terracotta" to "minecraft:{color}glazed_terracotta",
		"sand" to "minecraft:{color}sand",
		"sandstone" to "minecraft:{color}sandstone",
		"cut_sandstone" to "minecraft:cut_{color}sandstone",
		"smooth_sandstone" to "minecraft:smooth_{color}sandstone",
		"chiseled_sandstone" to "minecraft:chiseled_{color}sandstone",
		"sandstone_slab" to "minecraft:{color}sandstone_slab",
		"cut_sandstone_slab" to "minecraft:cut_{color}sandstone_slab",
		"smooth_sandstone_slab" to "minecraft:smooth_{color}sandstone_slab",
		"sandstone_stairs" to "minecraft:{color}sandstone_stairs",
		"smooth_sandstone_stairs" to "minecraft:smooth_{color}sandstone_stairs",
		"sandstone_walls" to "minecraft:{color}sandstone_walls",
		"shulker_box" to "minecraft:{color}shulker_box",
		"stained_glass" to "minecraft:{color}stained_glass",
		"stained_glass_pane" to "minecraft:{color}stained_glass_pane",
		"terracotta" to "minecraft:{color}terracotta",
		"tulip" to "minecraft:{color}tulip",
		"wool" to "minecraft:{color}wool"
	)

	val itemPatterns = mapOf(
		"dye" to "minecraft:{color}dye",
		"pigment" to "hexcasting:dye_colorizer_{color}"
	)

	private fun <T : Any> resolve(registry: Registry<T>, pattern: String, dye: DyeOption) =
		when {
			registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement)))) != null -> dye.ordinal to pattern.replace("{color}", dye.replacement)
			registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement + "_")))) != null -> dye.ordinal to pattern.replace("{color}", dye.replacement + "_")
			else -> null
		}

	val tasks = mutableListOf<(DataWriter, DataOutput) -> CompletableFuture<*>>()
	private fun generateFiles(json: JsonObject, path: String, name: String) {
		tasks.add { writer, output -> DataProvider.writeToPath(writer, json, output.getResolver(DataOutput.OutputType.DATA_PACK, path).resolve(HexicalMain.Companion.id(name), "json")) }
	}

	val patchouliPages = mutableListOf<JsonObject>()
	private fun generatePage(name: String, collection: Map<DyeOption, String>) {
		patchouliPages.add(JsonObject().apply {
			addProperty("type", "hexical:dyeing")
			addProperty("text", "hexical.recipe.dyeing.$name.text")
			collection.forEach { (dye, item) -> addProperty(dye.replacement.ifEmpty { "uncolored" }, item) }
		})
	}
}