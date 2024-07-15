package miyucomics.hexical.entities

import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix3f
import net.minecraft.util.math.Matrix4f
import kotlin.math.floor
import kotlin.math.max

class SpikeRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpikeEntity>(ctx) {
	private val textures = listOf(
		Identifier("textures/block/small_amethyst_bud.png"),
		Identifier("textures/block/medium_amethyst_bud.png"),
		Identifier("textures/block/large_amethyst_bud.png"),
		Identifier("textures/block/amethyst_cluster.png")
	)

	override fun getTexture(spike: SpikeEntity?): Identifier {
		return textures[max(floor(spike!!.getAnimationProgress() * (textures.size - 1)).toInt(), 0)]
	}

	override fun render(spike: SpikeEntity?, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		val buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(spike)))

		val direction = spike!!.getDirection()
		matrices.multiply(direction.rotationQuaternion)
		// I don't know why this works, but it works and I am never touching it again.
		val y = direction.offsetY.toDouble()
		matrices.translate(-0.5, -0.5 + y / 2, -1.0 + y * y / 2)

		val mat = matrices.peek().positionMatrix
		val norm = matrices.peek().normalMatrix

		vertex(mat, buffer, norm, 1f, 1f, 0f, 0f, 0f)
		vertex(mat, buffer, norm, 1f, 0f, 0f, 0f, 1f)
		vertex(mat, buffer, norm, 0f, 0f, 1f, 1f, 1f)
		vertex(mat, buffer, norm, 0f, 1f, 1f, 1f, 0f)
		vertex(mat, buffer, norm, 0f, 1f, 0f, 0f, 0f)
		vertex(mat, buffer, norm, 0f, 0f, 0f, 0f, 1f)
		vertex(mat, buffer, norm, 1f, 0f, 1f, 1f, 1f)
		vertex(mat, buffer, norm, 1f, 1f, 1f, 1f, 0f)

		matrices.pop()
	}

	private fun vertex(mat: Matrix4f, verts: VertexConsumer, normalMatrix: Matrix3f, x: Float, y: Float, z: Float, u: Float, v: Float) = verts.vertex(mat, x, y, z)
		.color(255, 255, 255, 255)
		.texture(u, v)
		.overlay(OverlayTexture.DEFAULT_UV)
		.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
		.normal(normalMatrix, 0.0f, 1.0f, 0.0f)
		.next()
}