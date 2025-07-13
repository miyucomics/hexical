package miyucomics.hexical

import miyucomics.hexical.features.items.MediaJarItemRenderer
import miyucomics.hexical.inits.*
import miyucomics.hexical.misc.AnimatedPatternTooltipComponent
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
		HexicalParticles.clientInit()
		HexicalRenderLayers.clientInit()
		HexicalHooks.clientInit()

		HexicalHooksClient.init()

		BuiltinItemRendererRegistry.INSTANCE.register(HexicalBlocks.MEDIA_JAR_ITEM, MediaJarItemRenderer())
		TooltipComponentCallback.EVENT.register(AnimatedPatternTooltipComponent::tryConvert)
	}
}