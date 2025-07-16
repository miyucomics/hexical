package miyucomics.hexical.features.specklikes

import at.petrak.hexcasting.api.HexAPI.modLoc
import miyucomics.hexical.misc.RenderUtils
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

class SpeckRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpeckEntity>(ctx) {
	override fun getTexture(entity: SpeckEntity?): Identifier? = null
	override fun shouldRender(entity: SpeckEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: SpeckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		matrices.translate(0.0, 0.25, 0.0)
		if (entity!!.clientIsText)
			matrices.translate(0.0, 0.125, 0.0)
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.yaw))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.pitch))
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.clientRoll))
		matrices.scale(entity.clientSize, entity.clientSize, entity.clientSize)

		if (entity.clientIsText) {
			matrices.scale(0.025f, -0.025f, 0.025f)
			val top = matrices.peek()
			val xOffset = -textRenderer.getWidth(entity.clientText) / 2f
			textRenderer.draw(entity.clientText, xOffset, 0f, -0x1, false, top.positionMatrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x00000000, light)
		} else {
			val top = matrices.peek()
			val buffer = vertexConsumers.getBuffer(renderLayer)
			fun makeVertex(pos: Vec2f) = buffer.vertex(top.positionMatrix, pos.x, pos.y, 0f)
				.color(entity.clientPigment.colorProvider.getColor(0f, Vec3d(pos.x.toDouble(), pos.y.toDouble(), 0.0).multiply(2.0).add(entity.pos)))
				.texture(0f, 0f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
				.normal(top.normalMatrix, 0f, 1f, 0f)
				.next()

			RenderUtils.quadifyLines(::makeVertex, entity.clientThickness * 0.05f / entity.clientSize, entity.clientVerts)
		}

		matrices.pop()
	}

	companion object {
		private val renderLayer = RenderLayer.getEntityCutoutNoCull(modLoc("textures/entity/white.png"))
	}
}