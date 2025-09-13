package miyucomics.hexical.features.dyes

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import java.io.InputStream
import java.io.InputStreamReader

object DyeDataHook : InitHook() {
	override fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object :
			SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("dyes")
			override fun reload(manager: ResourceManager) {
				blockGroups.clear()
				itemGroups.clear()
				blockDyeLookup.clear()
				itemDyeLookup.clear()
				manager.findResources("dyes") { path -> path.path.endsWith(".json") }.keys.forEach { id -> loadData(manager.getResource(id).get().inputStream) }
				println(blockDyeLookup)
			}
		})
	}

	fun isDyeable(block: Block): Boolean = blockDyeLookup.containsKey(block)
	fun isDyeable(item: Item): Boolean = itemDyeLookup.containsKey(item)
	fun getDye(block: Block): DyeOption? = blockDyeLookup[block]
	fun getDye(item: Item): DyeOption? = itemDyeLookup[item]

	fun getNewBlock(block: Block, dye: DyeOption): BlockState? {
		blockGroups.forEach { (_, group) ->
			if (group.containsValue(block) && group.containsKey(dye))
				return group[dye]!!.defaultState
		}
		return null
	}

	fun getNewItem(item: Item, dye: DyeOption): Item? {
		itemGroups.forEach { (_, group) ->
			if (group.containsValue(item) && group.containsKey(dye))
				return group[dye]!!
		}
		return null
	}

	private val blockGroups: HashMap<String, HashMap<DyeOption, Block>> = HashMap()
	private val itemGroups: HashMap<String, HashMap<DyeOption, Item>> = HashMap()
	private val blockDyeLookup = HashMap<Block, DyeOption>()
	private val itemDyeLookup = HashMap<Item, DyeOption>()

	private fun loadData(stream: InputStream) {
		val json = JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject

		val blocks = json.getAsJsonObject("blocks")
		blocks.keySet().forEach { groupName ->
			val pattern = blocks.getAsJsonPrimitive(groupName).asString
			val group = HashMap<DyeOption, Block>()
			DyeOption.values().forEach { dye -> resolvePattern(Registries.BLOCK, pattern, dye)?.let {
				group[dye] = it
				blockDyeLookup[it] = dye
			} }
			if (group.isNotEmpty())
				blockGroups[groupName] = group
		}

		val items = json.getAsJsonObject("items")
		items.keySet().forEach { groupName ->
			val pattern = items.getAsJsonPrimitive(groupName).asString
			val group = HashMap<DyeOption, Item>()
			DyeOption.values().forEach { dye -> resolvePattern(Registries.ITEM, pattern, dye)?.let {
				group[dye] = it
				itemDyeLookup[it] = dye
			} }
			if (group.isNotEmpty())
				itemGroups[groupName] = group
		}
	}

	private fun <T : Any> resolvePattern(registry: Registry<T>, pattern: String, dye: DyeOption) = listOfNotNull(
		registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement)))),
		registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement + "_"))))
	).firstOrNull()
}