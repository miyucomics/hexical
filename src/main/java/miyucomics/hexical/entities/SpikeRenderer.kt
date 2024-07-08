package miyucomics.hexical.entities

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix3f
import net.minecraft.util.math.Matrix4f

class SpikeRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpikeEntity>(ctx) {
	override fun getTexture(spike: SpikeEntity?) = Identifier("textures/block/amethyst_cluster.png")
	override fun render(spike: SpikeEntity?, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		val buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(spike)))
		val mat = matrices.peek().positionMatrix
		val norm = matrices.peek().normalMatrix

		vertex(mat, light, buffer, norm, 0.5f, 0f, -0.5f, 0f, 0f)
		vertex(mat, light, buffer, norm, 0.5f, -1f, -0.5f, 0f, 1f)
		vertex(mat, light, buffer, norm, -0.5f, -1f, 0.5f, 1f, 1f)
		vertex(mat, light, buffer, norm, -0.5f, 0f, 0.5f, 1f, 0f)

		vertex(mat, light, buffer, norm, -0.5f, 0f, -0.5f, 0f, 0f)
		vertex(mat, light, buffer, norm, -0.5f, -1f, -0.5f, 0f, 1f)
		vertex(mat, light, buffer, norm, 0.5f, -1f, 0.5f, 1f, 1f)
		vertex(mat, light, buffer, norm, 0.5f, 0f, 0.5f, 1f, 0f)

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