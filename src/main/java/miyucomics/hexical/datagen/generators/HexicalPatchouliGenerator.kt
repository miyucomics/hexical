package miyucomics.hexical.datagen.generators

import com.google.gson.JsonObject
import miyucomics.hexical.datagen.entry
import miyucomics.hexical.datagen.providers.DyeingProvider
import miyucomics.hexical.datagen.providers.FloraProvider
import miyucomics.hexical.datagen.providers.TransmutationProvider
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class HexicalPatchouliGenerator(val output: FabricDataOutput) : DataProvider {
	override fun getName() = "Hexical Patchouli Pages"
	override fun run(writer: DataWriter): CompletableFuture<*> = CompletableFuture.allOf(
		generateCurioPages(writer),
		generateDyeingPages(writer),
		generateFloraPages(writer),
		generateMediaJarPages(writer)
	)

	private fun generateCurioPages(writer: DataWriter): CompletableFuture<*> {
		return saveEntry(writer, entry("hexical.page.curios.title", HexicalItems.CURIO_STAFF) {
			sortnum = 10
			textPage("hexical.page.curios.0")
			HexicalItems.CURIO_NAMES.mapIndexed { i, name -> HexicalItems.CURIOS[i] to name }.forEach { (curio, name) -> spotlightPage(curio, "hexical.page.curio_$name.summary") }
		}, "items", "curios")
	}

	private fun generateDyeingPages(writer: DataWriter): CompletableFuture<*> {
		return saveEntry(writer, entry("hexical.page.dyes.title", Items.RED_DYE) {
			sortnum = 2
			category = "hexcasting:patterns/spells"
			textPage("hexical.page.dyes.0")
			patternPage("hexical:get_dye", "id/vector/entity", "dye/null", "hexical.page.get_dye.summary")
			patternPage("hexical:dye", "vector/entity, dye", "", "hexical.page.dye.summary")
			extraPages(DyeingProvider.patchouliPages)
		}, "patterns/spells", "dyeing")
	}

	private fun generateFloraPages(writer: DataWriter): CompletableFuture<*> {
		return saveEntry(writer, entry("hexical.page.flora.title", Items.POPPY) {
			sortnum = 10
			category = "hexcasting:patterns/spells"
			patternPage("hexical:conjure_flora", "vector, identifier", "", "hexical.page.conjure_flora.summary")
			textPage("hexical.page.conjure_flora.description")
			textPage("hexical.page.conjure_flora.0")
			extraPages(FloraProvider.recipePages)
		}, "patterns/spells", "conjure_flora")
	}

	private fun generateMediaJarPages(writer: DataWriter): CompletableFuture<*> {
		return saveEntry(writer, entry("hexical.page.media_jar.title", HexicalBlocks.MEDIA_JAR_ITEM) {
			sortnum = 4
			textPage("hexical.page.media_jar.0")
			craftPage("hexical:media_jar", "hexical.page.media_jar.description")
			textPage("hexical.page.media_jar.1")
			extraPages(TransmutationProvider.recipePages)
		}, "items", "media_jar")
	}

	private fun saveEntry(writer: DataWriter, json: JsonObject, path: String, name: String): CompletableFuture<*> =
		DataProvider.writeToPath(writer, json, output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "patchouli_books/thehexbook/en_us/entries/$path").resolve(Identifier("hexcasting", name), "json"))
}