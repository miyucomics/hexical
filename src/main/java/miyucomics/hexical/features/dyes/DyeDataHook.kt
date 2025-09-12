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
			override fun reload(manager: ResourceManager) = manager.findResources("dyes") { path -> path.path.endsWith(".json") }.keys.forEach { id -> loadData(manager.getResource(id).get().inputStream) }
		})
	}

	fun isDyeable(block: Block): Boolean = blockDyeLookup.containsKey(block)
	fun isDyeable(item: Item): Boolean = itemDyeLookup.containsKey(item)
	fun getDye(block: Block): DyeOptions? = blockDyeLookup[block]
	fun getDye(item: Item): DyeOptions? = itemDyeLookup[item]

	fun getNewBlock(block: Block, dye: DyeOptions): BlockState? {
		blockGroups.forEach { (_, group) ->
			if (group.containsValue(block) && group.containsKey(dye))
				return group[dye]!!.defaultState
		}
		return null
	}

	fun getNewItem(item: Item, dye: DyeOptions): Item? {
		itemGroups.forEach { (_, group) ->
			if (group.containsValue(item) && group.containsKey(dye))
				return group[dye]!!
		}
		return null
	}

	private val blockGroups: HashMap<String, HashMap<DyeOptions, Block>> = HashMap()
	private val itemGroups: HashMap<String, HashMap<DyeOptions, Item>> = HashMap()
	private val blockDyeLookup = HashMap<Block, DyeOptions>()
	private val itemDyeLookup = HashMap<Item, DyeOptions>()

	private fun loadData(stream: InputStream) {
		val json = JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject

		val blocks = json.getAsJsonObject("blocks")
		blocks.keySet().forEach { groupName ->
			val pattern = blocks.getAsJsonPrimitive(groupName).asString
			val group = HashMap<DyeOptions, Block>()
			DyeOptions.values().forEach { dye -> resolvePattern(Registries.BLOCK, pattern, dye)?.let {
				group[dye] = it
				blockDyeLookup[it] = dye
			} }
			if (group.isNotEmpty())
				blockGroups[groupName] = group
		}

		val items = json.getAsJsonObject("items")
		items.keySet().forEach { groupName ->
			val pattern = items.getAsJsonPrimitive(groupName).asString
			val group = HashMap<DyeOptions, Item>()
			DyeOptions.values().forEach { dye -> resolvePattern(Registries.ITEM, pattern, dye)?.let {
				group[dye] = it
				itemDyeLookup[it] = dye
			} }
			if (group.isNotEmpty())
				itemGroups[groupName] = group
		}
	}

	private fun <T : Any> resolvePattern(registry: Registry<T>, pattern: String, dye: DyeOptions) = listOfNotNull(
		registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.title)))),
		registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.title + "_"))))
	).firstOrNull()
}

enum class DyeOptions(val title: String) {
	UNCOLORED(""),
    WHITE("white"),
    ORANGE("orange"),
    MAGENTA("magenta"),
    LIGHT_BLUE("light_blue"),
    YELLOW("yellow"),
    LIME("lime"),
    PINK("pink"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    BROWN("brown"),
    GREEN("green"),
    RED("red"),
    BLACK("black")
}