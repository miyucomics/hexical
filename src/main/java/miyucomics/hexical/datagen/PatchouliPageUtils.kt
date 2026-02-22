package miyucomics.hexical.datagen

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.item.Item
import net.minecraft.registry.Registries

fun entry(title: String, icon: Item, block: EntryBuilder.() -> Unit): JsonObject {
	return EntryBuilder(title, icon).apply(block).build()
}

class EntryBuilder(val title: String, val icon: Item) {
	var category: String = "hexcasting:items"
	var advancement: String = "hexcasting:root"
	var sortnum: Int = 0
	private val pages = JsonArray()

	fun textPage(text: String) {
		pages.add(JsonObject().apply {
			addProperty("type", "patchouli:text")
			addProperty("text", text)
		})
	}

	fun patternPage(opId: String, input: String, output: String, text: String) {
		pages.add(JsonObject().apply {
			addProperty("type", "hexcasting:pattern")
			addProperty("op_id", opId)
			addProperty("anchor", opId)
			addProperty("input", input)
			addProperty("output", output)
			addProperty("text", text)
		})
	}

	fun craftPage(recipe: String, description: String) {
		pages.add(JsonObject().apply {
			addProperty("type", "patchouli:crafting")
			addProperty("recipe", recipe)
			addProperty("text", description)
		})
	}

	fun spotlightPage(item: Item, text: String) {
		pages.add(JsonObject().apply {
			addProperty("type", "patchouli:spotlight")
			addProperty("item", Registries.ITEM.getId(item).toString())
			addProperty("text", text)
		})
	}

	fun extraPages(providerPages: List<JsonObject>) {
		providerPages.forEach { pages.add(it) }
	}

	fun build(): JsonObject = JsonObject().apply {
		addProperty("name", title)
		addProperty("icon", Registries.ITEM.getId(icon).toString())
		addProperty("category", category)
		addProperty("advancement", advancement)
		addProperty("sortnum", sortnum)
		add("pages", pages)
	}
}