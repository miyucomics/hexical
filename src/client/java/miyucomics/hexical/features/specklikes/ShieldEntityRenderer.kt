package miyucomics.hexical.features.specklikes

import at.petrak.hexcasting.api.HexAPI
import miyucomics.hexical.features.shield.ShieldEntity
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import org.joml.Matrix3f
import org.joml.Matrix4f

class ShieldEntityRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<ShieldEntity>(ctx) {
	override fun getTexture(entity: ShieldEntity): Identifier? = null

	override fun render(shield: ShieldEntity, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-shield.yaw))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(shield.pitch))
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(shield.clientRoll))
		matrices.scale(shield.clientSize, shield.clientSize, shield.clientSize)

		val buffer = vertexConsumers.getBuffer(renderLayer)
		val pose = matrices.peek().positionMatrix
		val norm = matrices.peek().normalMatrix

		val height = 0.866f
		vertex(shield, pose, buffer, norm, 0f, height, 0.5f, 0f)
		vertex(shield, pose, buffer, norm, -0.5f, 0f, 0f, 1f)
		vertex(shield, pose, buffer, norm, 0.5f, 0f, 1f, 1f)
		vertex(shield, pose, buffer, norm, 0f, height, 0.5f, 0f)

		matrices.pop()
	}

	companion object {
		private val renderLayer = RenderLayer.getEntityCutoutNoCull(HexAPI.modLoc("textures/entity/white.png"))
		private fun vertex(shield: ShieldEntity, mat: Matrix4f, verts: VertexConsumer, normal: Matrix3f, x: Float, y: Float, u: Float, v: Float) {
			verts.vertex(mat, x, y, 0f)
				.color(shield.clientPigment.colorProvider.getColor(0f, Vec3d(x.toDouble() * 2, y.toDouble() * 2, 0.0)))
				.texture(u, v)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
				.normal(normal, 0f, 1f, 0f)
				.next()
		}
	}
}