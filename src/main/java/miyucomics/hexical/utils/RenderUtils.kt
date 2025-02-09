package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.client.render.rotate
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.*
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.*


object RenderUtils {
	private val NEGATIVE_X_ROTATION: Quaternionf = RotationAxis.POSITIVE_X.rotationDegrees(-90f)
	private val DIR2ROT: Map<Direction, Quaternionf> = enumValues<Direction>().associateWith { it.opposite.rotationQuaternion.mul(NEGATIVE_X_ROTATION) }

	private const val CIRCLE_RESOLUTION: Int = 20

	fun getNormalizedStrokes(pattern: HexPattern, flipHor: Boolean = false): List<Vec2f> {
		val lines = pattern.toLines(1f, pattern.getCenter(1f).negate()).toMutableList()
		val scaling = max(
			lines.maxBy { vector -> vector.x }.x - lines.minBy { vector -> vector.x }.x,
			lines.maxBy { vector -> vector.y }.y - lines.minBy { vector -> vector.y }.y
		)
		val xScale = if (flipHor) -1 else 1
		for (i in lines.indices)
			lines[i] = Vec2f(lines[i].x * xScale, -lines[i].y).multiply(1 / scaling)
		return lines.toList()
	}

	// takes a list of points, joins them with lines, and calls the vertex function passed in with all the vertices on those lines
	fun quadifyLines(vertex: (pos: Vec2f) -> Unit, thickness: Float, points: List<Vec2f>) {
		val pointCount = points.size
		if (pointCount < 2)
			return

		val joinAngles = FloatArray(pointCount)
		for (i in 2 until pointCount) {
			val currentPoint = points[i - 1]
			val offsetFromLast = currentPoint.add(points[i - 2].negate())
			val offsetToNext = points[i].add(currentPoint.negate())
			joinAngles[i - 1] = atan2(offsetFromLast.x * offsetToNext.y - offsetFromLast.y * offsetToNext.x, offsetFromLast.x * offsetToNext.x + offsetFromLast.y * offsetToNext.y)
		}

		for (i in 0 until pointCount - 1) {
			val currentPoint = points[i]
			val nextPoint = points[i + 1]

			val sideLength = nextPoint.add(currentPoint.negate()).normalize().multiply(thickness)
			val normal = Vec2f(-sideLength.y, sideLength.x)

			val currentDown = currentPoint.add(normal)
			val currentUp = currentPoint.add(normal.negate())
			val nextDown = nextPoint.add(normal)
			val nextUp = nextPoint.add(normal.negate())

			vertex(currentUp)
			vertex(currentDown)
			vertex(nextDown)
			vertex(nextUp)

			if (i > 0) {
				val angle = joinAngles[i]
				val joinSteps = ceil(abs(angle) / (2 * MathHelper.PI) * CIRCLE_RESOLUTION).toInt()
				if (joinSteps < 1)
					continue

				if (angle < 0) {
					var previous = currentPoint.add(normal)
					for (j in 1..joinSteps) {
						val fan = rotate(normal, -angle * (j.toFloat() / joinSteps))
						val fanShift = currentPoint.add(fan)

						vertex(currentPoint)
						vertex(currentPoint)
						vertex(fanShift)
						vertex(previous)
						previous = fanShift
					}
				} else if (angle > 0) {
					val reversedNormal = normal.negate()
					var previous = currentPoint.add(reversedNormal)
					for (j in 1..joinSteps) {
						val fan = rotate(reversedNormal, -angle * (j.toFloat() / joinSteps))
						val fanShift = currentPoint.add(fan)

						vertex(currentPoint)
						vertex(currentPoint)
						vertex(previous)
						vertex(fanShift)
						previous = fanShift
					}
				}
			}
		}

		for (pair in arrayOf(arrayOf(points[0], points[1]), arrayOf(points[pointCount - 1], points[pointCount - 2]))) {
			val point = pair[0]
			val prev = pair[1]

			val sideLength = point.add(prev.negate()).normalize().multiply(thickness)
			val normal = Vec2f(-sideLength.y, sideLength.x)
			val joinSteps = CIRCLE_RESOLUTION / 2
			for (j in joinSteps downTo 1) {
				val fan0 = rotate(normal, -PI.toFloat() * (j.toFloat() / joinSteps))
				val fan1 = rotate(normal, -PI.toFloat() * ((j - 1).toFloat() / joinSteps))

				vertex(point)
				vertex(point)
				vertex(point.add(fan1))
				vertex(point.add(fan0))
			}
		}
	}

	fun addRect(consumer: VertexConsumer, matrices: MatrixStack, minU: Float, minV: Float, maxU: Float, maxV: Float, width: Float, height: Float, light: Int, color: Int, alpha: Float) {
		val minV2 = maxV - width
		val halfWidth = width / 2f
		val halfHeight = height / 2f

		val red = ColorHelper.Abgr.getRed(color)
		val green = ColorHelper.Abgr.getGreen(color)
		val blue = ColorHelper.Abgr.getBlue(color)
		val alphaInt = (255 * alpha).toInt()

		matrices.push()
		matrices.translate(0f, halfHeight, 0f)

		for (direction in Direction.values()) {
			var vStart = minV
			var depth = halfWidth
			var y0 = -halfHeight
			var y1 = halfHeight

			if (direction.axis == Direction.Axis.Y) {
				depth = halfHeight
				y0 = -halfWidth
				y1 = halfWidth
				vStart = minV2
			}

			matrices.push()
			matrices.multiply(DIR2ROT[direction])
			matrices.translate(0f, 0f, -depth)
			addQuad(consumer, matrices, -halfWidth, y0, halfWidth, y1, minU, vStart, maxU, maxV, red, green, blue, alphaInt, light)
			matrices.pop()
		}

		matrices.pop()
	}

	private fun addQuad(consumer: VertexConsumer, matrices: MatrixStack, x0: Float, y0: Float, x1: Float, y1: Float, u0: Float, v0: Float, u1: Float, v1: Float, red: Int, green: Int, blue: Int, alpha: Int, light: Int) {
		val normal = matrices.peek().normalMatrix.transform(Vector3f(0f, 0f, -1f))
		val nx = normal.x
		val ny = normal.y
		val nz = normal.z
		vertex(consumer, matrices, x0, y1, 0f, u0, v0, red, green, blue, alpha, light, nx, ny, nz)
		vertex(consumer, matrices, x1, y1, 0f, u1, v0, red, green, blue, alpha, light, nx, ny, nz)
		vertex(consumer, matrices, x1, y0, 0f, u1, v1, red, green, blue, alpha, light, nx, ny, nz)
		vertex(consumer, matrices, x0, y0, 0f, u0, v1, red, green, blue, alpha, light, nx, ny, nz)
	}

	private fun vertex(consumer: VertexConsumer, matrices: MatrixStack, x: Float, y: Float, z: Float, u: Float, v: Float, red: Int, green: Int, blue: Int, alpha: Int, light: Int, nx: Float, ny: Float, nz: Float) {
		consumer.vertex(matrices.peek().positionMatrix, x, y, z)
			.color(red, green, blue, alpha)
			.texture(u, v)
			.overlay(0, 10)
			.light(light)
			.normal(nx, ny, nz)
			.next()
	}
}