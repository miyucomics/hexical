package miyucomics.hexical.entities

import at.petrak.hexcasting.api.HexAPI.modLoc
import at.petrak.hexcasting.api.misc.FrozenColorizer
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import kotlin.math.cos
import kotlin.math.sin

class MeshRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<MeshEntity>(ctx) {
	private val sides = 6
	private val angleIncrement = 2 * Math.PI / sides
	private val layer = RenderLayer.getEntityCutoutNoCull(modLoc("textures/entity/white.png"))

	override fun getTexture(entity: MeshEntity?): Identifier? = null
	override fun shouldRender(entity: MeshEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: MeshEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		val vertices = entity!!.clientVertices
		if (vertices.size < 2)
			return

		matrices.push()
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-entity.pitch))
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - entity.yaw))
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(entity.clientRoll))
		matrices.scale(entity.clientSize, entity.clientSize, entity.clientSize)

		val buf = vertexConsumers.getBuffer(layer)
		for (i in 1..<vertices.size) {
			val a = vertices[i - 1]
			val b = vertices[i]
			drawConnection(matrices, buf, Vec3d(a.x.toDouble(), a.y.toDouble(), a.z.toDouble()), Vec3d(b.x.toDouble(), b.y.toDouble(), b.z.toDouble()), entity.clientPigment, entity.clientThickness * 0.025)
		}
		matrices.pop()
	}

	private fun drawConnection(matrices: MatrixStack, vertices: VertexConsumer, start: Vec3d, end: Vec3d, pigment: FrozenColorizer, thickness: Double) {
		val direction = end.subtract(start).normalize()
		var perpendicular = direction.crossProduct(Vec3d(1.0, 0.0, 0.0))
		if (direction.dotProduct(Vec3d(1.0, 0.0, 0.0)) > 0.99)
			perpendicular = direction.crossProduct(Vec3d(0.0, 1.0, 0.0))

		val position = matrices.peek().positionMatrix
		val normal = matrices.peek().normalMatrix

		for (i in 0 until sides) {
			val angle1 = i * angleIncrement
			val angle2 = (i + 1) % sides * angleIncrement

			val a = perpendicular.multiply(cos(angle1)).add(perpendicular.crossProduct(direction).multiply(sin(angle1))).add(direction.multiply(direction.dotProduct(perpendicular)).multiply(1 - cos(angle1))).normalize().multiply(thickness)
			val b = perpendicular.multiply(cos(angle2)).add(perpendicular.crossProduct(direction).multiply(sin(angle2))).add(direction.multiply(direction.dotProduct(perpendicular)).multiply(1 - cos(angle2))).normalize().multiply(thickness)

			val point1 = start.add(a)
			val point2 = start.add(b)
			val point3 = end.add(b)
			val point4 = end.add(a)

			vertices.vertex(position, point1.x.toFloat(), point1.y.toFloat(), point1.z.toFloat()).color(pigment.getColor(0f, point1)).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()
			vertices.vertex(position, point2.x.toFloat(), point2.y.toFloat(), point2.z.toFloat()).color(pigment.getColor(0f, point2)).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()
			vertices.vertex(position, point3.x.toFloat(), point3.y.toFloat(), point3.z.toFloat()).color(pigment.getColor(0f, point3)).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()
			vertices.vertex(position, point4.x.toFloat(), point4.y.toFloat(), point4.z.toFloat()).color(pigment.getColor(0f, point4)).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normal, 0f, 1f, 0f).next()
		}
	}
}