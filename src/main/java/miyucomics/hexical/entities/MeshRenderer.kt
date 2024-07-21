package miyucomics.hexical.entities

import at.petrak.hexcasting.api.HexAPI.modLoc
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

class MeshRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<MeshEntity>(ctx) {
	private val layer = RenderLayer.getEntityCutoutNoCull(modLoc("textures/entity/white.png"))

	override fun getTexture(entity: MeshEntity?): Identifier? = null
	override fun shouldRender(entity: MeshEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: MeshEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		val vertices = entity!!.clientVertices
		if (vertices.size < 2)
			return

		val buf = vertexConsumers.getBuffer(layer)
		for (i in 1..<vertices.size) {
			val a = vertices[i - 1]
			val b = vertices[i]
			renderLine(matrices, buf, Vec3d(a.x.toDouble(), a.y.toDouble(), a.z.toDouble()), Vec3d(b.x.toDouble(), b.y.toDouble(), b.z.toDouble()), light)
		}
	}

	private fun renderLine(matrices: MatrixStack, buffer: VertexConsumer, start: Vec3d, end: Vec3d, light: Int) {
		val position = matrices.peek().positionMatrix
		val normal = matrices.peek().normalMatrix

		val strokeDirection = end.subtract(start).normalize()
		val cameraDirection = MinecraftClient.getInstance().gameRenderer.camera.pos.subtract(start.add(end).multiply(0.5)).normalize()
		val perpendicular = strokeDirection.crossProduct(cameraDirection).normalize().multiply(0.05)

		val p1 = start.add(perpendicular)
		val p2 = start.subtract(perpendicular)
		val p3 = end.subtract(perpendicular)
		val p4 = end.add(perpendicular)

		buffer.vertex(position, p1.x.toFloat(), p1.y.toFloat(), p1.z.toFloat()).color(1f, 1f, 1f, 1f).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, 0f, 0f, 1f).next()
		buffer.vertex(position, p2.x.toFloat(), p2.y.toFloat(), p2.z.toFloat()).color(1f, 1f, 1f, 1f).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, 0f, 0f, 1f).next()
		buffer.vertex(position, p3.x.toFloat(), p3.y.toFloat(), p3.z.toFloat()).color(1f, 1f, 1f, 1f).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, 0f, 0f, 1f).next()
		buffer.vertex(position, p4.x.toFloat(), p4.y.toFloat(), p4.z.toFloat()).color(1f, 1f, 1f, 1f).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, 0f, 0f, 1f).next()
	}
}