package miyucomics.hexical

import com.samsthenerd.inline.impl.extrahooks.ItemOverlayManager
import miyucomics.hexical.features.animated_scrolls.AnimatedPatternTooltipComponent
import miyucomics.hexical.features.charms.CharmedItemIconRenderer
import miyucomics.hexical.features.media_jar.MediaJarItemRenderer
import miyucomics.hexical.inits.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback

class HexicalClient : ClientModInitializer {
	override fun onInitializeClient() {
		HexicalRenderLayers.clientInit()
		HexicalEntitiesClient.clientInit()
		HexicalHooksClient.init()
		HexicalKeybinds.clientInit()
		HexicalParticlesClient.clientInit()

		ClientTickEvents.END_CLIENT_TICK.register { ClientStorage.ticks += 1 }
		BuiltinItemRendererRegistry.INSTANCE.register(HexicalBlocks.MEDIA_JAR_ITEM, MediaJarItemRenderer())
		TooltipComponentCallback.EVENT.register(AnimatedPatternTooltipComponent::tryConvert)
		ItemOverlayManager.addRenderer(CharmedItemIconRenderer)
	}
}