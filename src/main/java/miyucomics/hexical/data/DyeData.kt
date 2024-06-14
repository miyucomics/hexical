package miyucomics.hexical.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.collections.HashMap

object DyeData {
	private val flatBlockLookup = HashMap<String, String>()
	private val flatItemLookup = HashMap<String, String>()
	private val blockFamilies = HashMap<String, MutableMap<String, String>>()
	private val itemFamilies = HashMap<String, MutableMap<String, String>>()

	fun isDyeable(block: Block): Boolean = flatBlockLookup.containsKey(Registry.BLOCK.getId(block).toString())
	fun isDyeable(item: Item): Boolean = flatItemLookup.containsKey(Registry.ITEM.getId(item).toString())
	fun getDye(block: Block): String? = flatBlockLookup.getOrDefault(Registry.BLOCK.getId(block).toString(), null)
	fun getDye(item: Item): String? = flatItemLookup.getOrDefault(Registry.ITEM.getId(item).toString(), null)

	fun getNewBlock(block: Block, dye: String): BlockState {
		blockFamilies.forEach { (_, family) ->
			if (family.containsValue(Registry.BLOCK.getId(block).toString()) && family.containsKey(dye))
				return Registry.BLOCK.get(Identifier(family[dye])).defaultState
		}
		return block.defaultState
	}
	fun getNewItem(item: Item, dye: String): Item {
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
						flatBlockLookup[block] = family.get(block).asString
						if (!blockFamilies.containsKey(familyKey))
							blockFamilies[familyKey] = mutableMapOf()
						blockFamilies[familyKey]!![family.get(block).asString] = block
					}
				}
			}
			"dyes/items.json" -> {
				json.keySet().forEach { familyKey ->
					val family = json.get(familyKey) as JsonObject
					family.keySet().forEach { item ->
						flatItemLookup[item] = family.get(item).asString
						if (!itemFamilies.containsKey(familyKey))
							itemFamilies[familyKey] = mutableMapOf()
						itemFamilies[familyKey]!![family.get(item).asString] = item
					}
				}
			}
		}
	}
}