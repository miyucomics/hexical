package miyucomics.hexical.utils

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.client.CAP_THETA
import at.petrak.hexcasting.client.rotate
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.*
import kotlin.math.*

object RenderUtils {
	// this rendering function draws some points similar to a sentinel: it will always be the same width regardless of distance
	fun sentinelLike(matrices: MatrixStack, points: List<Vec3d>, width: Float, colorizer: FrozenColorizer) {
		if (points.size <= 1) return
		val tessellator = Tessellator.getInstance()
		val buf = tessellator.buffer
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader)
		RenderSystem.lineWidth(width)
		val matrix = matrices.peek().positionMatrix
		fun vertex(a: Vec3d, b: Vec3d) {
			val aColor = colorizer.getColor(0f, a)
			val bColor = colorizer.getColor(0f, b)
			val normal = a.subtract(b).normalize()
			buf.vertex(matrix, a.x.toFloat(), a.y.toFloat(), a.z.toFloat())
				.color(aColor)
				.normal(matrices.peek().normalMatrix, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
				.next()
			buf.vertex(matrix, b.x.toFloat(), b.y.toFloat(), b.z.toFloat())
				.color(bColor)
				.normal(matrices.peek().normalMatrix, -normal.x.toFloat(), -normal.y.toFloat(), -normal.z.toFloat())
				.next()
		}
		buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES)
		for (i in 1 until points.size) {
			val a = points[i - 1]
			val b = points[i]
			vertex(Vec3d(a.x, a.y, a.z), Vec3d(b.x, b.y, b.z))
		}
		tessellator.draw()
	}

	fun drawFigure(mat: Matrix4f, points: List<Vec2f>, width: Float, colorizer: FrozenColorizer) {
		if (points.size <= 1) return

		val tessellator = Tessellator.getInstance()
		val buf = tessellator.buffer
		val n = points.size
		val joinAngles = FloatArray(n)
		for (i in 2 until n) {
			val previousPoint = points[i - 2]
			val currentPoint = points[i - 1]
			val nextPoint = points[i]
			val offsetFromLast = currentPoint.add(previousPoint.negate())
			val offsetToNext = nextPoint.add(currentPoint.negate())

			// angle of "vector from previous point to current point" and "vector from current point to next point"
			val angle = atan2(offsetFromLast.x * offsetToNext.y - offsetFromLast.y * offsetToNext.x, offsetFromLast.x * offsetToNext.x + offsetFromLast.y * offsetToNext.y)
			joinAngles[i - 1] = angle
		}

		fun vertex(pos: Vec2f) {
			val color = colorizer.getColor(0f, Vec3d(pos.x.toDouble(), pos.y.toDouble(), 0.0))
			buf.vertex(mat, pos.x, pos.y, 0f).color(color).next()
		}

		buf.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR)
		for (i in 0 until points.size - 1) {
			val currentPoint = points[i]
			val nextPoint = points[i + 1]

			val sideLength = nextPoint.add(currentPoint.negate()).normalize().multiply(width * 0.5f)
			val normal = Vec2f(-sideLength.y, sideLength.x)

			val p1Down = currentPoint.add(normal)
			val p1Up = currentPoint.add(normal.negate())
			val p2Down = nextPoint.add(normal)
			val p2Up = nextPoint.add(normal.negate())

			vertex(p1Down)
			vertex(p1Up)
			vertex(p2Up)

			vertex(p1Down)
			vertex(p2Up)
			vertex(p2Down)

			if (i > 0) {
				val sangle = joinAngles[i]
				val angle = abs(sangle)
				val rnormal = normal.negate()
				val joinSteps = ceil(angle * 180 / (CAP_THETA * MathHelper.PI)).toInt()
				if (joinSteps < 1) {
					continue
				}

				if (sangle < 0) {
					var prevVert = Vec2f(currentPoint.x - rnormal.x, currentPoint.y - rnormal.y)
					for (j in 1..joinSteps) {
						val fan = rotate(rnormal, -sangle * (j.toFloat() / joinSteps.toFloat()))
						val fanShift = Vec2f(currentPoint.x - fan.x, currentPoint.y - fan.y)
						vertex(currentPoint)
						vertex(prevVert)
						vertex(fanShift)
						prevVert = fanShift
					}
				} else {
					val startFan = rotate(normal, -sangle)
					var prevVert = Vec2f(currentPoint.x - startFan.x, currentPoint.y - startFan.y)
					for (j in joinSteps - 1 downTo 0) {
						val fan = rotate(normal, -sangle * (j.toFloat() / joinSteps))
						val fanShift = Vec2f(currentPoint.x - fan.x, currentPoint.y - fan.y)

						vertex(currentPoint)
						vertex(prevVert)
						vertex(fanShift)
						prevVert = fanShift
					}
				}
			}
		}
		tessellator.draw()

		fun drawCaps(point: Vec2f, prev: Vec2f) {
			val sideLength = point.add(prev.negate()).normalize().multiply(0.5f * width)
			val normal = Vec2f(-sideLength.y, sideLength.x)
			val joinSteps = MathHelper.ceil(180f / CAP_THETA)
			buf.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
			vertex(point)
			for (j in joinSteps downTo 0) {
				val fan = rotate(normal, -MathHelper.PI * (j.toFloat() / joinSteps))
				vertex(Vec2f(point.x + fan.x, point.y + fan.y))
			}
			tessellator.draw()
		}
		drawCaps(points[0], points[1])
		drawCaps(points[n - 1], points[n - 2])
	}
}