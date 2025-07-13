package miyucomics.hexical

import miyucomics.hexical.client.AnimatedPatternTooltipComponent
import miyucomics.hexical.features.items.MediaJarItemRenderer
import miyucomics.hexical.inits.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback

class HexicalClient : ClientModInitializer {
	override fun onInitializeClient() {
		HexicalBlocks.clientInit()
		HexicalEntities.clientInit()
		HexicalEvents.clientInit()
		HexicalKeybinds.clientInit()
		HexicalItems.clientInit()
		HexicalNetworking.clientInit()
		HexicalParticles.clientInit()
		HexicalRenderLayers.clientInit()

		BuiltinItemRendererRegistry.INSTANCE.register(HexicalBlocks.MEDIA_JAR_ITEM, MediaJarItemRenderer())
		TooltipComponentCallback.EVENT.register(AnimatedPatternTooltipComponent::tryConvert)
	}
}