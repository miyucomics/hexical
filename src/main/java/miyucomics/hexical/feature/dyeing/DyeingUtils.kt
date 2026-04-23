package miyucomics.hexical.feature.dyeing

import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier

object DyeingUtils : InitHook() {
	private val blocks = DyeRegistryWrapper(Registries.BLOCK, "dyeing/block")
	private val items = DyeRegistryWrapper(Registries.ITEM, "dyeing/item")

	fun getDye(block: Block) = blocks.dyeOf(block)
	fun getDye(item: Item): DyeOption? = items.dyeOf(item) ?: (item as? BlockItem)?.let { blocks.dyeOf(it.block) }

	fun getResult(block: Block, dye: DyeOption) = blocks.convert(block, dye)
	fun getResult(stack: ItemStack, dye: DyeOption): ItemStack? {
		val item = items.convert(stack.item, dye)
			?: (stack.item as? BlockItem)?.let { blocks.convert(it.block, dye) }?.asItem()
			?: return null
		return ItemStack(item, stack.count).also { it.nbt = stack.nbt?.copy() }
	}

	override fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("dyes")
			override fun reload(manager: ResourceManager) {
				blocks.reload(manager)
				items.reload(manager)
			}
		})
	}
}

private class DyeRegistryWrapper<T : Any>(private val registry: Registry<T>, private val folder: String) {
	private val dyes = mutableMapOf<T, DyeOption>()
	private val families = mutableMapOf<T, Map<DyeOption, T>>()

	fun dyeOf(thing: T): DyeOption? = dyes[thing]
	fun convert(thing: T, dye: DyeOption): T? = families[thing]?.get(dye)

	fun reload(manager: ResourceManager) {
		dyes.clear()
		families.clear()
		manager.findResources(folder) { it.path.endsWith(".json") }.values.forEach { resource ->
			val json = resource.reader.use { JsonParser.parseReader(it).asJsonObject }
			val family = json.entrySet().mapNotNull { (key, value) ->
				val dye = DyeOption.fromKey(key) ?: return@mapNotNull null
				val id = Identifier.tryParse(value.asString) ?: return@mapNotNull null
				val thing = registry.getOrEmpty(id).orElse(null) ?: return@mapNotNull null
				dye to thing
			}.toMap()

			family.forEach { (dye, thing) ->
				dyes[thing] = dye
				families[thing] = family
			}
		}
	}
}