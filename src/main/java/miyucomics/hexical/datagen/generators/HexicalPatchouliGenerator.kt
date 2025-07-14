package miyucomics.hexical.datagen.generators

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import miyucomics.hexical.datagen.Transmutations
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class HexicalPatchouliGenerator(val output: FabricDataOutput) : DataProvider {
	override fun run(writer: DataWriter): CompletableFuture<*> {
		val finalJson = JsonObject().apply {
			addProperty("name", "hexical.page.media_jar.title")
			addProperty("icon", "hexical:media_jar")
			addProperty("advancement", "hexcasting:root")
			addProperty("category", "hexcasting:items")
			addProperty("sortnum", 4)
			add("pages", JsonArray().apply {
				add(JsonObject().apply {
					addProperty("type", "patchouli:text")
					addProperty("text", "hexical.page.media_jar.0")
				})
				add(JsonObject().apply {
					addProperty("type", "patchouli:crafting")
					addProperty("recipe", "hexical:media_jar")
					addProperty("text", "hexical.page.media_jar.description")
				})
				add(JsonObject().apply {
					addProperty("type", "patchouli:text")
					addProperty("text", "hexical.page.media_jar.1")
				})
				Transmutations.transmutationRecipePages.forEach { add(it) }
			})
		}

		val path = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "patchouli_books/thehexbook/en_us/entries/items")
		return CompletableFuture.allOf(DataProvider.writeToPath(writer, finalJson, path.resolve(Identifier("hexcasting", "media_jar"), "json")))
	}

	override fun getName(): String = "Patchouli Pages"
}