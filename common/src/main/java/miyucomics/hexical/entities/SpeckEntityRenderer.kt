package miyucomics.hexical.entities

import at.petrak.hexcasting.client.rotate
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SpeckEntityRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<SpeckEntity?>(ctx) {
	override fun render(entity: SpeckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int) {
		matrices!!.push()
		val mat = matrices.peek()
		theCoolerDrawLineSeq(mat.positionMatrix, mat.normalMatrix, light, vertexConsumers!!.getBuffer(RenderLayer.getEntityCutout(Identifier("hexcasting", "textures/entity/white.png"))), entity!!.getPattern(), 0.025f, 0x9368d2)
		matrices.pop()
	}

	override fun getTexture(entity: SpeckEntity?): Identifier? {
		return null
	}

	private fun vertexCol(mat: Matrix4f, normal: Matrix3f, light: Int, verts: VertexConsumer, col: Int, pos: Vec2f) {
		verts.vertex(mat, -pos.x, pos.y, 0f)
			.color(col)
			.texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light)
			.normal(normal, 0f, 0f, 1f)
			.next()
	}

	private fun theCoolerDrawLineSeq(
		mat: Matrix4f, normalMat: Matrix3f, light: Int, verts: VertexConsumer,
		points: List<Vec2f>, width: Float, color: Int
	) {
		if (points.size <= 1) {
			return
		}

		// TODO: abstract some of this out with RenderLib to stop WET code
		val joinAngles = FloatArray(points.size)
		val joinOffsets = FloatArray(points.size)
		for (i in 2 until points.size) {
			val p0 = points[i - 2]
			val p1 = points[i - 1]
			val p2 = points[i]
			val prev = p1.add(p0.negate())
			val next = p2.add(p1.negate())
			val angle = MathHelper.atan2(
				(prev.x * next.y - prev.y * next.x).toDouble(),
				(prev.x * next.x + prev.y * next.y).toDouble()
			).toFloat()
			joinAngles[i - 1] = angle
			val clamp = (min(prev.length().toDouble(), next.length().toDouble()) / (width * 0.5f)).toFloat()
			joinOffsets[i - 1] = MathHelper.clamp(MathHelper.sin(angle) / (1 + MathHelper.cos(angle)), -clamp, clamp)
		}

		for (i in 0 until points.size - 1) {
			val p1 = points[i]
			val p2 = points[i + 1]

			val tangent = p2.add(p1.negate()).normalize().multiply(width * 0.5f)
			val normal = Vec2f(-tangent.y, tangent.x)

			val jlow = joinOffsets[i]
			val jhigh = joinOffsets[i + 1]

			val p1Down = p1.add(tangent.multiply(max(0.0, jlow.toDouble()).toFloat())).add(normal)
			val p1Up = p1.add(tangent.multiply(max(0.0, -jlow.toDouble()).toFloat())).add(normal.negate())
			val p2Down = p2.add(tangent.multiply(max(0.0, jhigh.toDouble()).toFloat()).negate()).add(normal)
			val p2Up = p2.add(tangent.multiply(max(0.0, -jhigh.toDouble()).toFloat()).negate()).add(normal.negate())

			// Draw the chamfer hexagon as two trapezoids
			// the points are in different orders to keep clockwise
			vertexCol(mat, normalMat, light, verts, color, p1)
			vertexCol(mat, normalMat, light, verts, color, p2)
			vertexCol(mat, normalMat, light, verts, color, p2Up)
			vertexCol(mat, normalMat, light, verts, color, p1Up)

			vertexCol(mat, normalMat, light, verts, color, p1)
			vertexCol(mat, normalMat, light, verts, color, p1Down)
			vertexCol(mat, normalMat, light, verts, color, p2Down)
			vertexCol(mat, normalMat, light, verts, color, p2)

			if (i > 0) {
				val sangle = joinAngles[i]
				val angle = abs(sangle.toDouble()).toFloat()
				val rnormal = normal.negate()
				val joinSteps = MathHelper.ceil(angle * 180 / (180f / 10f * MathHelper.PI))
				if (joinSteps < 1) continue

				if (sangle < 0) {
					var prevVert = Vec2f(p1.x - rnormal.x, p1.y - rnormal.y)
					for (j in 1..joinSteps) {
						val fan = rotate(rnormal, -sangle * (j.toFloat() / joinSteps))
						val fanShift = Vec2f(p1.x - fan.x, p1.y - fan.y)

						vertexCol(mat, normalMat, light, verts, color, p1)
						vertexCol(mat, normalMat, light, verts, color, p1)
						vertexCol(mat, normalMat, light, verts, color, fanShift)
						vertexCol(mat, normalMat, light, verts, color, prevVert)
						prevVert = fanShift
					}
				} else {
					val startFan = rotate(normal, -sangle)
					var prevVert = Vec2f(p1.x - startFan.x, p1.y - startFan.y)
					for (j in joinSteps - 1 downTo 0) {
						val fan = rotate(normal, -sangle * (j.toFloat() / joinSteps))
						val fanShift = Vec2f(p1.x - fan.x, p1.y - fan.y)

						vertexCol(mat, normalMat, light, verts, color, p1)
						vertexCol(mat, normalMat, light, verts, color, p1)
						vertexCol(mat, normalMat, light, verts, color, fanShift)
						vertexCol(mat, normalMat, light, verts, color, prevVert)
						prevVert = fanShift
					}
				}
			}
		}

		for (pair in arrayOf<Array<Vec2f>>(
			arrayOf<Vec2f>(
				points[0], points[1]
			),
			arrayOf<Vec2f>(points[points.size - 1], points[points.size - 2])
		)) {
			val point = pair[0]
			val prev = pair[1]

			val tangent = point.add(prev.negate()).normalize().multiply(0.5f * width)
			val normal = Vec2f(-tangent.y, tangent.x)
			val joinSteps = MathHelper.ceil(180f / 180f / 10f)
			for (j in joinSteps downTo 1) {
				val fan0 = rotate(normal, -MathHelper.PI * (j.toFloat() / joinSteps))
				val fan1 = rotate(normal, -MathHelper.PI * ((j - 1).toFloat() / joinSteps))

				vertexCol(mat, normalMat, light, verts, color, point)
				vertexCol(mat, normalMat, light, verts, color, point)
				vertexCol(mat, normalMat, light, verts, color, point.add(fan1))
				vertexCol(mat, normalMat, light, verts, color, point.add(fan0))
			}
		}
	}
}