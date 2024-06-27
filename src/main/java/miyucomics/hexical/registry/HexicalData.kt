package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.data.DyeData
import miyucomics.hexical.data.PrestidigitationData
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType

object HexicalData {
	@JvmStatic
	fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("dyes")
			override fun reload(manager: ResourceManager) = manager.findResources("dyes") { path -> path.path.endsWith(".json") }.keys.forEach { id -> DyeData.loadData(manager.getResource(id).get().inputStream) }
		})
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("prestidigitation")
			override fun reload(manager: ResourceManager) = manager.findResources("prestidigitation") { path -> path.path.endsWith(".json") }.keys.forEach { id -> PrestidigitationData.loadData(manager.getResource(id).get().inputStream) }
		})
	}
}