package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import java.io.IOException

object HexicalRenderLayers {
	lateinit var UV_VISUALIZER_LAYER: RenderLayer
	lateinit var uvVisualizerShader: ShaderProgram
	val PERLIN_NOISE: Identifier = HexicalMain.id("textures/misc/perlin.png")

	fun clientInit() {
		CoreShaderRegistrationCallback.EVENT.register { context ->
			try {
				uvVisualizerShader = ShaderProgram(MinecraftClient.getInstance().resourceManager, "rendertype_media_jar", VertexFormats.POSITION_COLOR_TEXTURE);
				UV_VISUALIZER_LAYER = RenderLayer.of(
					"uv_visualizer_layer",
					VertexFormats.POSITION_COLOR_TEXTURE,
					VertexFormat.DrawMode.QUADS,
					256,
					RenderLayer.MultiPhaseParameters.builder()
						.program(RenderPhase.ShaderProgram { uvVisualizerShader })
						.texture(RenderPhase.Textures.create()
							.add(PERLIN_NOISE, false, false)
							.build())
						.transparency(RenderPhase.NO_TRANSPARENCY)
						.cull(RenderPhase.ENABLE_CULLING)
						.lightmap(RenderPhase.DISABLE_LIGHTMAP)
						.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
						.build(true)
				)
			} catch (e: IOException) {
				throw RuntimeException("Failed to load UV visualizer shader", e)
			}
		}
	}
}