package miyucomics.hexical.entities

import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.decoration.AbstractDecorationEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix3f
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f

class SpikeRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpikeEntity>(ctx) {
	override fun getTexture(spike: SpikeEntity?) = Identifier("textures/block/amethyst_cluster.png")
	override fun render(spike: SpikeEntity?, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		val buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(spike)))

		val direction = spike!!.getDirection()
		val offset = Vec3d((direction.offsetX.toFloat() * 0.5), 0.0, (direction.offsetZ.toFloat() * 0.5))
		matrices.translate(-offset.x, -offset.y, -offset.z)
		matrices.translate(direction.offsetX.toDouble() / 2, direction.offsetY.toDouble() / 2, direction.offsetZ.toDouble() / 2)
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(spike.pitch))
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - spike.yaw))
		matrices.translate(-0.5, -0.5, -0.5)

		val form = ((spike.world.time.toInt() % 10) + deltaTick) / 10

		val mat = matrices.peek().positionMatrix
		val norm = matrices.peek().normalMatrix

		vertex(mat, light, buffer, norm, 1f, 1f, 0f, 0f, 0f)
		vertex(mat, light, buffer, norm, 1f, 0f, 0f, 0f, form)
		vertex(mat, light, buffer, norm, 0f, 0f, 1f, 1f, form)
		vertex(mat, light, buffer, norm, 0f, 1f, 1f, 1f, 0f)

		vertex(mat, light, buffer, norm, 0f, 1f, 0f, 0f, 0f)
		vertex(mat, light, buffer, norm, 0f, 0f, 0f, 0f, form)
		vertex(mat, light, buffer, norm, 1f, 0f, 1f, 1f, form)
		vertex(mat, light, buffer, norm, 1f, 1f, 1f, 1f, 0f)

		matrices.pop()
	}

	private fun vertex(mat: Matrix4f, light: Int, verts: VertexConsumer, normalMatrix: Matrix3f, x: Float, y: Float, z: Float, u: Float, v: Float) = verts.vertex(mat, x, y, z)
		.color(255, 255, 255, 255)
		.texture(u, v)
		.overlay(OverlayTexture.DEFAULT_UV)
		.light(light)
		.normal(normalMatrix, 0.0f, 1.0f, 0.0f)
		.next()
}