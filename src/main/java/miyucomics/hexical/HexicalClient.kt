package miyucomics.hexical

import miyucomics.hexical.client.AnimatedPatternTooltipComponent
import miyucomics.hexical.registry.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats

class HexicalClient : ClientModInitializer {
	override fun onInitializeClient() {
		HexicalBlocks.clientInit()
		HexicalEntities.clientInit()
		HexicalEvents.clientInit()
		HexicalKeybinds.clientInit()
		HexicalItems.clientInit()
		HexicalNetworking.clientInit()
		HexicalParticles.clientInit()

		TooltipComponentCallback.EVENT.register(AnimatedPatternTooltipComponent::tryConvert)
	}

	companion object {
		val MEDIA_JAR_RENDER_LAYER: RenderLayer = RenderLayer.of(
			"media_jar",
			VertexFormats.POSITION,
			VertexFormat.DrawMode.QUADS,
			256,
			false,
			false,
			MultiPhaseParameters.builder()
				.program(RenderPhase.ShaderProgram { ShaderProgram(MinecraftClient.getInstance().resourceManager, "rendertype_media_jar", VertexFormats.POSITION) })
				.writeMaskState(RenderPhase.COLOR_MASK)
				.depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
				.cull(RenderPhase.DISABLE_CULLING)
				.build(false)
		)
	}
}