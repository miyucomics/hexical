package miyucomics.hexical.features.media_jar

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.Hook
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier

object MediaJarShader : Hook() {
	lateinit var mediaJarRenderLayer: RenderLayer
	val PERLIN_NOISE: Identifier = HexicalMain.id("textures/misc/perlin.png")

	override fun registerCallbacks() {
		CoreShaderRegistrationCallback.EVENT.register { context ->
			context.register(HexicalMain.id("media_jar"), VertexFormats.POSITION_TEXTURE_COLOR_NORMAL) { shader ->
				mediaJarRenderLayer = RenderLayer.of(
					"media_jar_shader",
					VertexFormats.POSITION_TEXTURE_COLOR_NORMAL,
					VertexFormat.DrawMode.QUADS,
					512,
					RenderLayer.MultiPhaseParameters.builder()
						.program(RenderPhase.ShaderProgram { shader })
						.texture(RenderPhase.Textures.create()
							.add(PERLIN_NOISE, false, false)
							.build())
						.transparency(RenderPhase.NO_TRANSPARENCY)
						.cull(RenderPhase.ENABLE_CULLING)
						.lightmap(RenderPhase.DISABLE_LIGHTMAP)
						.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
						.build(true)
				)
			}
		}
	}
}