package miyucomics.hexical.registry

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.data.DyeData
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.DyeColor
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

object HexicalData {
	@JvmStatic
	fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("hexical_dyes")
			override fun reload(manager: ResourceManager) {
				manager.findResources("hexical_dyes") { path -> path.path.endsWith(".json") }.keys.forEach { id ->
					try {
						val stream = manager.getResource(id).get().inputStream
						val json = JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject
						json.keySet().forEach { familyKey ->
							val family = json.get(familyKey) as JsonObject
							family.keySet().forEach { block ->
								val dye = DyeColor.byName(family.get(block).asString, DyeColor.WHITE)!!
								DyeData.blocks[block] = dye
								if (!DyeData.families.containsKey(familyKey))
									DyeData.families[familyKey] = EnumMap(DyeColor::class.java)
								DyeData.families[familyKey]!![dye] = block
							}
						}
					} catch (e: IOException) {
						error("Error occurred while loading resource json $id $e")
					}
				}
			}
		})
	}
}