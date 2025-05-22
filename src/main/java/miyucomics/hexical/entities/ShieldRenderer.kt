package miyucomics.hexical.entities

import at.petrak.hexcasting.api.HexAPI.modLoc
import net.minecraft.client.render.*
import net.minecraft.client.render.RenderPhase.TRANSLUCENT_TRANSPARENCY
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import org.joml.Matrix3f
import org.joml.Matrix4f

class ShieldRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<ShieldEntity>(ctx) {
	override fun getTexture(entity: ShieldEntity): Identifier? = null

	override fun render(entity: ShieldEntity?, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity!!.yaw))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.pitch))

		val buffer = vertexConsumers.getBuffer(renderLayer)
		val pose = matrices.peek().positionMatrix
		val norm = matrices.peek().normalMatrix


		val height = 0.866f
		vertex(pose, buffer, norm, 0f, height / 2, 0f, 0.5f, 0f)
		vertex(pose, buffer, norm, -0.5f, -height / 2, 0f, 0f, 1f)
		vertex(pose, buffer, norm, 0.5f, -height / 2, 0f, 1f, 1f)
		vertex(pose, buffer, norm, 0f, height / 2, 0f, 0.5f, 0f)

		matrices.pop()
	}

	companion object {
		private val renderLayer = RenderLayer.getEntityTranslucent(modLoc("textures/entity/white.png"))
		private fun vertex(mat: Matrix4f, verts: VertexConsumer, normal: Matrix3f, x: Float, y: Float, z: Float, u: Float, v: Float) = verts.vertex(mat, x, y, z)
			.color(255, 255, 255, 250)
			.texture(u, v)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
			.normal(normal, 0f, 1f, 0f)
			.next()
	}
}