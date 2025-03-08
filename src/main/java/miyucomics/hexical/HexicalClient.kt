package miyucomics.hexical

import miyucomics.hexical.registry.*
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer

class HexicalClient : ClientModInitializer {
	override fun onInitializeClient() {
		HexicalBlocks.clientInit()
		HexicalEntities.clientInit()
		HexicalEvents.clientInit()
		HexicalKeybinds.clientInit()
		HexicalItems.clientInit()
		HexicalNetworking.clientInit()
		HexicalParticles.clientInit()
	}

	companion object {
		private val TEXTURE = MultiPhaseParameters.builder()
			.program(RenderPhase.ShaderProgram { ShaderProgram(MinecraftClient.getInstance().resourceManager, "rendertype_media_jar", VertexFormats.POSITION) })
			.texture(RenderPhase.Textures.create().add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).build())
			.build(false)
		val MEDIA_JAR_RENDER_LAYER: RenderLayer = RenderLayer.of("media_jar", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, TEXTURE)
	}
}