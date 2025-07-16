package miyucomics.hexical

import miyucomics.hexical.features.animated_scrolls.AnimatedPatternTooltipComponent
import miyucomics.hexical.features.media_jar.MediaJarItemRenderer
import miyucomics.hexical.inits.*
import miyucomics.hexical.misc.ClientStorage
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback

class HexicalClient : ClientModInitializer {
	override fun onInitializeClient() {
		HexicalBlocks.clientInit()
		HexicalEntities.clientInit()
		HexicalKeybinds.clientInit()
		HexicalParticles.clientInit()

		HexicalHooksClient.init()

		ClientTickEvents.END_CLIENT_TICK.register { ClientStorage.ticks += 1 }
		BuiltinItemRendererRegistry.INSTANCE.register(HexicalBlocks.MEDIA_JAR_ITEM, MediaJarItemRenderer())
		TooltipComponentCallback.EVENT.register(AnimatedPatternTooltipComponent::tryConvert)
	}
}