package miyucomics.hexical.datagen.providers

import com.google.gson.JsonObject
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.feature.dyeing.DyeOption
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

object DyeingProvider {
	val blockPatterns = mapOf(
		"bed" to "minecraft:{color_}bed",
		"candle" to "minecraft:{color_}candle",
		"candle_cake" to "minecraft:{color_}candle_cake",
		"carpet" to "minecraft:{color_}carpet",
		"carpeted_pedestal" to "hexical:{color_}carpeted_pedestal",
		"concrete" to "minecraft:{color_}concrete",
		"concrete_powder" to "minecraft:{color_}concrete_powder",
		"glazed_terracotta" to "minecraft:{color_}glazed_terracotta",
		"sand" to "minecraft:{color_}sand",
		"sandstone" to "minecraft:{color_}sandstone",
		"cut_sandstone" to "minecraft:cut_{color_}sandstone",
		"smooth_sandstone" to "minecraft:smooth_{color_}sandstone",
		"chiseled_sandstone" to "minecraft:chiseled_{color_}sandstone",
		"sandstone_slab" to "minecraft:{color_}sandstone_slab",
		"cut_sandstone_slab" to "minecraft:cut_{color_}sandstone_slab",
		"smooth_sandstone_slab" to "minecraft:smooth_{color_}sandstone_slab",
		"sandstone_stairs" to "minecraft:{color_}sandstone_stairs",
		"smooth_sandstone_stairs" to "minecraft:smooth_{color_}sandstone_stairs",
		"sandstone_wall" to "minecraft:{color_}sandstone_wall",
		"shulker_box" to "minecraft:{color_}shulker_box",
		"stained_glass" to "minecraft:{color_}stained_glass",
		"stained_glass_pane" to "minecraft:{color_}stained_glass_pane",
		"terracotta" to "minecraft:{color_}terracotta",
		"tulip" to "minecraft:{color_}tulip",
		"wool" to "minecraft:{color_}wool"
	)

	val itemPatterns = mapOf(
		"dye" to "minecraft:{color_}dye",
		"pigment" to "hexcasting:dye_colorizer_{color}"
	)

	val tasks = mutableListOf<(DataWriter, DataOutput) -> CompletableFuture<*>>()
	val patchouliPages = mutableListOf<JsonObject>()

	fun init() {
		generate("item", Registries.ITEM, "hexical:dyeing_item", itemPatterns)
		generate("block", Registries.BLOCK, "hexical:dyeing_block", blockPatterns)
	}

	private fun <T : Any> generate(kind: String, registry: Registry<T>, pageType: String, patterns: Map<String, String>) {
		patterns.forEach { (group, pattern) ->
			val members = DyeOption.entries.mapNotNull { dye ->
				val id = pattern.replace("{color_}", dye.prefix).replace("{color}", dye.slug)
				if (registry.containsId(Identifier(id))) dye to id else null
			}

			val data = JsonObject().apply { members.forEach { (dye, id) -> addProperty(dye.key, id) } }
			tasks += { writer, output -> DataProvider.writeToPath(writer, data, output.getResolver(DataOutput.OutputType.DATA_PACK, "dyeing/$kind/").resolve(HexicalMain.id(group), "json")) }

			patchouliPages += JsonObject().apply {
				addProperty("type", pageType)
				addProperty("text", "hexical.recipe.dyeing.$group.text")
				members.forEach { (dye, id) -> addProperty(dye.key, id) }
			}
		}
	}
}