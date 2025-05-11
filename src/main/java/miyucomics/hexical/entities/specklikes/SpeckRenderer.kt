package miyucomics.hexical.entities.specklikes

import at.petrak.hexcasting.api.HexAPI.modLoc
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import org.joml.Matrix3f
import org.joml.Matrix4f

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

		val top = matrices.peek()
		if (entity.clientIsText) {
			RenderSystem.disableCull()
			val overlayConsumer = OverlayAwareVertexConsumerProvider(vertexConsumers)
			val height = (-textRenderer.getWidth(entity.clientText) / 2).toFloat()
			matrices.scale(0.025f, -0.025f, 0.025f)
			textRenderer.draw(entity.clientText, height, 0f, -1, false, top.positionMatrix, overlayConsumer, TextRenderer.TextLayerType.NORMAL, 0, light)
			RenderSystem.enableCull()
		} else {
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

private class OverlayAwareVertexConsumerProvider(private val victim: VertexConsumerProvider) : VertexConsumerProvider {
	override fun getBuffer(layer: RenderLayer): VertexConsumer {
		val originalConsumer = victim.getBuffer(layer)
		return OverlayAwareVertexConsumer(originalConsumer)
	}

	private class OverlayAwareVertexConsumer(private val victim: VertexConsumer) : VertexConsumer {
		private var didOverlay = false

		override fun color(red: Int, green: Int, blue: Int, alpha: Int): VertexConsumer = victim.color(red, green, blue, alpha)
		override fun fixedColor(i: Int, j: Int, k: Int, l: Int) = victim.fixedColor(i, j, k, l)
		override fun light(u: Int, v: Int): VertexConsumer = victim.light(u, v)
		override fun normal(x: Float, y: Float, z: Float): VertexConsumer = victim.normal(x, y, z)
		override fun normal(matrix: Matrix3f, x: Float, y: Float, z: Float): VertexConsumer	= victim.normal(matrix, x, y, z)
		override fun texture(u: Float, v: Float): VertexConsumer = victim.texture(u, v)
		override fun unfixColor() =	victim.unfixColor()
		override fun vertex(x: Double, y: Double, z: Double): VertexConsumer  = victim.vertex(x, y, z)
		override fun vertex(matrix: Matrix4f, x: Float, y: Float, z: Float): VertexConsumer = victim.vertex(matrix, x, y, z)

		override fun overlay(u: Int, v: Int): VertexConsumer {
			didOverlay = true
			return victim.overlay(u, v)
		}

		override fun next() {
			if (!didOverlay)
				victim.overlay(OverlayTexture.DEFAULT_UV)
			victim.next()
			didOverlay = false
		}
	}
}