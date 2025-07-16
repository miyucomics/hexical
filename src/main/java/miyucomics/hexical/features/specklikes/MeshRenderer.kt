package miyucomics.hexical.features.specklikes

import at.petrak.hexcasting.api.HexAPI.modLoc
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import org.joml.Matrix3f
import org.joml.Matrix4f
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalStdlibApi::class)
class MeshRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<MeshEntity>(ctx) {
	override fun getTexture(entity: MeshEntity?): Identifier? = null
	override fun shouldRender(entity: MeshEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: MeshEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		val vertices = entity!!.clientVertices
		if (vertices.size < 2)
			return

		matrices.push()
		matrices.translate(0.0, 0.25, 0.0)
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.yaw))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.pitch))
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.clientRoll))
		matrices.scale(entity.clientSize, entity.clientSize, entity.clientSize)

		val buf = vertexConsumers.getBuffer(renderLayer)
		for (i in 1..<vertices.size) {
			val a = vertices[i - 1]
			val b = vertices[i]
			drawConnection(matrices, buf, Vec3d(a.x.toDouble(), a.y.toDouble(), a.z.toDouble()), Vec3d(b.x.toDouble(), b.y.toDouble(), b.z.toDouble()), entity.clientPigment, entity.clientThickness * 0.025)
		}
		matrices.pop()
	}

	private fun drawConnection(matrices: MatrixStack, vertices: VertexConsumer, start: Vec3d, end: Vec3d, pigment: FrozenPigment, thickness: Double) {
		val direction = end.subtract(start).normalize()
		var perpendicular = direction.crossProduct(Vec3d(1.0, 0.0, 0.0))
		if (direction.dotProduct(Vec3d(1.0, 0.0, 0.0)) > 0.99 || direction.dotProduct(Vec3d(1.0, 0.0, 0.0)) < -0.99)
			perpendicular = direction.crossProduct(Vec3d(0.0, 1.0, 0.0))

		val pose = matrices.peek().positionMatrix
		val norm = matrices.peek().normalMatrix

		// these calculations are reused often but could just be ran once
		val perpendicularCrossProduct = perpendicular.crossProduct(direction)
		val directionDotProduct = direction.multiply(direction.dotProduct(perpendicular))

		for (i in 0 until SIDES) {
			val startAngle = i * ANGLE_INCREMENT
			val endAngle = (i + 1) % SIDES * ANGLE_INCREMENT
			val a = perpendicular.multiply(cos(startAngle)).add(perpendicularCrossProduct.multiply(sin(startAngle))).add(directionDotProduct.multiply(1 - cos(startAngle))).normalize().multiply(thickness)
			val b = perpendicular.multiply(cos(endAngle)).add(perpendicularCrossProduct.multiply(sin(endAngle))).add(directionDotProduct.multiply(1 - cos(endAngle))).normalize().multiply(thickness)

			vertex(pose, norm, vertices, start.add(a), pigment)
			vertex(pose, norm, vertices, start.add(b), pigment)
			vertex(pose, norm, vertices, end.add(b), pigment)
			vertex(pose, norm, vertices, end.add(a), pigment)
		}
	}

	private fun vertex(pose: Matrix4f, norm: Matrix3f, vertices: VertexConsumer, position: Vec3d, pigment: FrozenPigment) {
		vertices.vertex(pose, position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
			.color(pigment.colorProvider.getColor(0f, position))
			.texture(0f, 0f)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
			.normal(norm, 0f, 1f, 0f)
			.next()
	}

	companion object {
		private const val SIDES = 6
		private const val ANGLE_INCREMENT = 2 * Math.PI / SIDES
		private val renderLayer = RenderLayer.getEntityCutoutNoCull(modLoc("textures/entity/white.png"))
	}
}