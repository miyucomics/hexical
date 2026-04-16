package miyucomics.hexical

import miyucomics.hexical.feature.media_jar.MediaJarItemRenderer
import miyucomics.hexical.init.*
import miyucomics.hexical.misc.ClientStorage
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry

class HexicalClient : ClientModInitializer {
	override fun onInitializeClient() {
		HexicalRenderLayers.clientInit()
		HexicalBlocksClient.clientInit()
		HexicalEntitiesClient.clientInit()
		HexicalHooksClient.init()
		HexicalKeybinds.clientInit()
		HexicalParticlesClient.clientInit()

		ClientTickEvents.END_CLIENT_TICK.register { ClientStorage.ticks += 1 }
		BuiltinItemRendererRegistry.INSTANCE.register(HexicalBlocks.MEDIA_JAR_ITEM, MediaJarItemRenderer)
	}
}