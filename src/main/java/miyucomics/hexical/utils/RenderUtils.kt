package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.client.render.rotate
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec2f
import kotlin.math.*


object RenderUtils {
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
}