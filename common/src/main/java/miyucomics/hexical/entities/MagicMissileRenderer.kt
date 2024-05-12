package miyucomics.hexical.entities

import miyucomics.hexical.Hexical
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix3f
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Vec3f

class MagicMissileRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<MagicMissileEntity>(ctx) {
	override fun render(missile: MagicMissileEntity, yaw: Float, deltaTick: Float, matrixStack: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, light: Int) {
		matrixStack.push()
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(deltaTick, missile.prevYaw, missile.yaw) - 90.0f))
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(deltaTick, missile.prevPitch, missile.pitch)))
		matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0f))
		matrixStack.scale(0.05625f, 0.05625f, 0.05625f)
		matrixStack.translate(-4.0, 0.0, 0.0)
		val vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(missile)))
		val entry = matrixStack.peek()
		val matrix4f = entry.positionMatrix
		val matrix3f = entry.normalMatrix
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, light)
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, light)
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, light)
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, light)
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, light)
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, light)
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, light)
		this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, light)
		for (u in 0..3) {
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f))
			this.vertex(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, light)
			this.vertex(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, light)
			this.vertex(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, light)
			this.vertex(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, light)
		}
		matrixStack.pop()
		super.render(missile, yaw, deltaTick, matrixStack, vertexConsumerProvider, light)
	}

	override fun getTexture(entity: MagicMissileEntity?): Identifier = Hexical.id("textures/entity/magic_missile.png")
	private fun vertex(positionMatrix: Matrix4f?, normalMatrix: Matrix3f?, vertexConsumer: VertexConsumer, x: Int, y: Int, z: Int, u: Float, v: Float, normalX: Int, normalZ: Int, normalY: Int, light: Int) {
		vertexConsumer.vertex(positionMatrix, x.toFloat(), y.toFloat(), z.toFloat()).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, normalX.toFloat(), normalY.toFloat(), normalZ.toFloat()).next()
	}
}