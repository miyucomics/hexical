package miyucomics.hexical.features.mute

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalCardinalComponents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

object MuteRenderer {
	@JvmStatic
	fun render(entity: Entity, matrices: MatrixStack, vertices: VertexConsumerProvider, dispatcher: EntityRenderDispatcher) {
		if (!HexicalCardinalComponents.MUTED_COMPONENT.get(entity).muted)
			return
		val time = entity.world.time + MinecraftClient.getInstance().tickDelta

		matrices.push()
		matrices.translate(0f, entity.height + MathHelper.sin(time * 0.05f) * 0.1f + 0.5f, 0f)
		matrices.multiply(dispatcher.rotation)
		matrices.scale(0.5f, 0.5f, 0.5f)

		val buffer = vertices.getBuffer(RenderLayer.getEntityCutout(ICON))
		val entry = matrices.peek()
		val pos = entry.getPositionMatrix()
		val normal = entry.getNormalMatrix()

		buffer.vertex(pos, -0.5f, -0.5f, 0f).color(255, 255, 255, 255).texture(1f, 1f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()
		buffer.vertex(pos, -0.5f, 0.5f, 0f).color(255, 255, 255, 255).texture(1f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()
		buffer.vertex(pos, 0.5f, 0.5f, 0f).color(255, 255, 255, 255).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()
		buffer.vertex(pos, 0.5f, -0.5f, 0f).color(255, 255, 255, 255).texture(0f, 1f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()

		matrices.pop()
	}

	private val ICON: Identifier = HexicalMain.id("textures/mob_effect/muted.png")
}