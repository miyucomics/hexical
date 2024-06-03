package miyucomics.hexical.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.HashMap

object DyeData {
	private val flatBlockLookup = HashMap<String, DyeColor?>()
	private val flatItemLookup = HashMap<String, DyeColor?>()
	private val blockFamilies = HashMap<String, MutableMap<DyeColor?, String>>()
	private val itemFamilies = HashMap<String, MutableMap<DyeColor?, String>>()

	fun isDyeable(block: Block): Boolean { return flatBlockLookup.containsKey(Registry.BLOCK.getId(block).toString()) }
	fun isDyeable(item: Item): Boolean { return flatItemLookup.containsKey(Registry.ITEM.getId(item).toString()) }
	fun getDye(block: Block): DyeColor? { return flatBlockLookup.getOrDefault(Registry.BLOCK.getId(block).toString(), null) }
	fun getDye(item: Item): DyeColor? { return flatItemLookup.getOrDefault(Registry.ITEM.getId(item).toString(), null) }

	fun getNewBlock(block: Block, dye: DyeColor?): BlockState {
		blockFamilies.forEach { (_, family) ->
			if (family.containsValue(Registry.BLOCK.getId(block).toString()) && family.containsKey(dye))
				return Registry.BLOCK.get(Identifier(family[dye])).defaultState
		}
		return block.defaultState
	}
	fun getNewItem(item: Item, dye: DyeColor?): Item {
		itemFamilies.forEach { (_, family) ->
			if (family.containsValue(Registry.ITEM.getId(item).toString()) && family.containsKey(dye))
				return Registry.ITEM.get(Identifier(family[dye]))
		}
		return item
	}

	fun loadData(stream: InputStream, name: String) {
		val json = JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject
		when (name) {
			"dyes/blocks.json" -> {
				json.keySet().forEach { familyKey ->
					val family = json.get(familyKey) as JsonObject
					family.keySet().forEach { block ->
						val raw = family.get(block).asString
						val dye = if (raw == "uncolored") null else DyeColor.byName(family.get(block).asString, DyeColor.WHITE)!!
						flatBlockLookup[block] = dye
						if (!blockFamilies.containsKey(familyKey))
							blockFamilies[familyKey] = mutableMapOf()
						blockFamilies[familyKey]!![dye] = block
					}
				}
			}
			"dyes/items.json" -> {
				json.keySet().forEach { familyKey ->
					val family = json.get(familyKey) as JsonObject
					family.keySet().forEach { item ->
						val raw = family.get(item).asString
						val dye = if (raw == "uncolored") null else DyeColor.byName(family.get(item).asString, DyeColor.WHITE)!!
						flatItemLookup[item] = dye
						if (!itemFamilies.containsKey(familyKey))
							itemFamilies[familyKey] = mutableMapOf()
						itemFamilies[familyKey]!![dye] = item
					}
				}
			}
		}
	}
}