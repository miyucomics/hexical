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
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier
import java.io.InputStream
import java.io.InputStreamReader

object DyeingUtils : InitHook() {
	val blockDyeLookup = mutableMapOf<Block, DyeOption>()
	val itemDyeLookup = mutableMapOf<Item, DyeOption>()
	val blockConvertLookup = mutableMapOf<Block, Map<DyeOption, Block>>()
	val itemConvertLookup = mutableMapOf<Item, Map<DyeOption, Item>>()

	fun getDye(block: Block) = blockDyeLookup[block]
	fun getDye(item: Item) = itemDyeLookup[item]

	fun getResult(block: Block, dye: DyeOption): Block? = blockConvertLookup[block]?.get(dye)

	fun getResult(stack: ItemStack, dye: DyeOption): ItemStack? {
		val newStack = ItemStack(itemConvertLookup[stack.item]?.get(dye) ?: return null, stack.count)
		if (stack.hasNbt())
			newStack.nbt = stack.nbt
		return newStack
	}

	override fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("dyes")
			override fun reload(manager: ResourceManager) {
				blockDyeLookup.clear()
				blockConvertLookup.clear()
				manager.findResources("dyeing/block") { it.path.endsWith(".json") }.values.forEach { load(it.inputStream, Registries.BLOCK, blockDyeLookup, blockConvertLookup) }

				itemDyeLookup.clear()
				itemConvertLookup.clear()
				manager.findResources("dyeing/item") { it.path.endsWith(".json") }.values.forEach { load(it.inputStream, Registries.ITEM, itemDyeLookup, itemConvertLookup) }
			}
		})
	}

	private fun <T : Any> load(stream: InputStream, registry: Registry<T>, dyeLookup: MutableMap<T, DyeOption>, convertLookup: MutableMap<T, Map<DyeOption, T>>) {
		val allMatches = (JsonParser.parseReader(InputStreamReader(stream, Charsets.UTF_8)) as JsonObject).asMap().filterKeys { registry.containsId(Identifier(it)) }.map { (id, dyeElement) ->
			val obj = registry.get(Identifier(id))!!
			val dye = enumValues<DyeOption>()[dyeElement.asInt]
			dyeLookup[obj] = dye
			dye to obj
		}.toMap()
		allMatches.values.forEach { convertLookup[it] = allMatches }
	}

	private fun <T : Comparable<T>> copyProperty(property: Property<T>, from: BlockState, to: BlockState): BlockState {
		return if (to.contains(property)) to.with(property, from.get(property)) else to
	}
}