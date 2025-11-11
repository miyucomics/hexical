package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalBlocks.PERIWINKLE_FLOWER
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats

object HexicalRenderLayers {
	val PERLIN_NOISE_TEXTURE: RenderPhase.Textures = RenderPhase.Textures.create().add(HexicalMain.id("textures/misc/perlin.png"), false, false).build()

	private lateinit var mageBlockShader: ShaderProgram
	val mageBlockRenderLayer: RenderLayer = RenderLayer.of(
		"mage_block",
		VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
		VertexFormat.DrawMode.QUADS,
		2097152,
		true,
		false,
		MultiPhaseParameters.builder()
			.lightmap(RenderPhase.ENABLE_LIGHTMAP)
			.program(RenderPhase.ShaderProgram { mageBlockShader })
			.texture(PERLIN_NOISE_TEXTURE)
			.build(true)
	)

	private lateinit var mediaJarShader: ShaderProgram
	val mediaJarRenderLayer: RenderLayer = RenderLayer.of(
		"media_jar",
		VertexFormats.POSITION_TEXTURE_COLOR_NORMAL,
		VertexFormat.DrawMode.QUADS,
		512,
		MultiPhaseParameters.builder()
			.program(RenderPhase.ShaderProgram { mediaJarShader })
			.texture(PERLIN_NOISE_TEXTURE)
			.transparency(RenderPhase.NO_TRANSPARENCY)
			.cull(RenderPhase.ENABLE_CULLING)
			.lightmap(RenderPhase.DISABLE_LIGHTMAP)
			.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
			.build(true)
	)

	fun clientInit() {
		CoreShaderRegistrationCallback.EVENT.register { context ->
			context.register(HexicalMain.id("media_jar"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL) { mageBlockShader = it }
			context.register(HexicalMain.id("media_jar"), VertexFormats.POSITION_TEXTURE_COLOR_NORMAL) { mediaJarShader = it }
		}

		BlockRenderLayerMap.INSTANCE.putBlock(HexicalBlocks.MAGE_BLOCK, mageBlockRenderLayer)
		BlockRenderLayerMap.INSTANCE.putBlock(PERIWINKLE_FLOWER, RenderLayer.getCutout())
	}
}