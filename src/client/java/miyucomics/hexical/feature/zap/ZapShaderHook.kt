package miyucomics.hexical.feature.zap

import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.texture.TextureManager

object ZapShaderHook : InitHook() {
	lateinit var ZAP_SHADER: ShaderProgram

	val ZAP_SHEET: ParticleTextureSheet = object : ParticleTextureSheet {
		override fun begin(builder: BufferBuilder, textureManager: TextureManager) {
			RenderSystem.depthMask(true)
			RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE)
			RenderSystem.enableBlend()
			RenderSystem.defaultBlendFunc()
			RenderSystem.setShader { ZAP_SHADER }
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT)
		}

		override fun draw(tessellator: Tessellator) = tessellator.draw()
		override fun toString() = "Hexical Zapping"
	}

	override fun init() {
		CoreShaderRegistrationCallback.EVENT.register() { context ->
			context.register(HexicalMain.id("zap"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT) { ZAP_SHADER = it }
		}
	}
}