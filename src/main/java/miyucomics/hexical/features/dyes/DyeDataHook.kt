package miyucomics.hexical.features.dyes

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import java.io.InputStream
import java.io.InputStreamReader

object DyeDataHook : InitHook() {
	private val flatBlockLookup = HashMap<String, String>()
	private val flatItemLookup = HashMap<String, String>()
	private val blockFamilies = HashMap<String, MutableMap<String, String>>()
	private val itemFamilies = HashMap<String, MutableMap<String, String>>()

	override fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object :
			SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("dyes")
			override fun reload(manager: ResourceManager) = manager.findResources("dyes") { path -> path.path.endsWith(".json") }.keys.forEach { id -> loadData(manager.getResource(id).get().inputStream) }
		})
	}

	fun isDyeable(block: Block): Boolean = flatBlockLookup.containsKey(Registries.BLOCK.getId(block).toString())
	fun isDyeable(item: Item): Boolean = flatItemLookup.containsKey(Registries.ITEM.getId(item).toString())
	fun getDye(block: Block): String? = flatBlockLookup[Registries.BLOCK.getId(block).toString()]
	fun getDye(item: Item): String? = flatItemLookup[Registries.ITEM.getId(item).toString()]

	fun getNewBlock(block: Block, dye: String): BlockState {
		blockFamilies.forEach { (_, family) ->
			if (family.containsValue(Registries.BLOCK.getId(block).toString()) && family.containsKey(dye))
				return Registries.BLOCK.get(Identifier(family[dye])).defaultState
		}
		return block.defaultState
	}

	fun getNewItem(item: Item, dye: String): Item {
		itemFamilies.forEach { (_, family) ->
			if (family.containsValue(Registries.ITEM.getId(item).toString()) && family.containsKey(dye))
				return Registries.ITEM.get(Identifier(family[dye]))
		}
		return item
	}

	private fun loadData(stream: InputStream) {
		val json = JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject

		val blocks = json.getAsJsonObject("blocks")
		blocks.keySet().forEach { familyKey ->
			val family = blocks.getAsJsonObject(familyKey)
			family.keySet().forEach { block ->
				flatBlockLookup[block] = family.get(block).asString
				if (!blockFamilies.containsKey(familyKey))
					blockFamilies[familyKey] = mutableMapOf()
				blockFamilies[familyKey]!![family.get(block).asString] = block
			}
		}

		val items = json.getAsJsonObject("items")
		items.keySet().forEach { familyKey ->
			val family = items.getAsJsonObject(familyKey)
			family.keySet().forEach { item ->
				flatItemLookup[item] = family.get(item).asString
				if (!itemFamilies.containsKey(familyKey))
					itemFamilies[familyKey] = mutableMapOf()
				itemFamilies[familyKey]!![family.get(item).asString] = item
			}
		}
	}
}