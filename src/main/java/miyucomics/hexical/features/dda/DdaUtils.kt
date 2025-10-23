package miyucomics.hexical.features.dda

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.abs
import kotlin.math.floor

object DdaUtils {
	fun raycast(position: Vec3d, direction: Vec3d, n: Int): Pair<List<BlockPos>, List<Vec3d>> {
		val positions = mutableListOf<BlockPos>()
		val normals = mutableListOf<Vec3d>()

		val delta = Vec3d(abs(1 / direction.x), abs(1 / direction.y), abs(1 / direction.z))

		var voxelX = floor(position.x).toInt()
		var voxelY = floor(position.y).toInt()
		var voxelZ = floor(position.z).toInt()
		val stepX: Int
		val stepY: Int
		val stepZ: Int
		var sideDistanceX: Double
		var sideDistanceY: Double
		var sideDistanceZ: Double

		if (direction.x > 0) {
			stepX = 1
			sideDistanceX = (voxelX + 1 - position.x) * delta.x
		} else {
			stepX = -1
			sideDistanceX = (position.x - voxelX) * delta.x
		}

		if (direction.y > 0) {
			stepY = 1
			sideDistanceY = (voxelY + 1 - position.y) * delta.y
		} else {
			stepY = -1
			sideDistanceY = (position.y - voxelY) * delta.y
		}

		if (direction.z > 0) {
			stepZ = 1
			sideDistanceZ = (voxelZ + 1 - position.z) * delta.z
		} else {
			stepZ = -1
			sideDistanceZ = (position.z - voxelZ) * delta.z
		}

		for (i in 0 until n) {
			val (axis, step, offset) = when {
				sideDistanceX < sideDistanceY && sideDistanceX < sideDistanceZ -> Triple('X', stepX, delta.x)
				sideDistanceY < sideDistanceZ -> Triple('Y', stepY, delta.y)
				else -> Triple('Z', stepZ, delta.z)
			}

			val normal = when (axis) {
				'X' -> {
					voxelX += step
					sideDistanceX += offset
					Vec3d(-step.toDouble(), 0.0, 0.0)
				}
				'Y' -> {
					voxelY += step
					sideDistanceY += offset
					Vec3d(0.0, -step.toDouble(), 0.0)
				}
				'Z' -> {
					voxelZ += step
					sideDistanceZ += offset
					Vec3d(0.0, 0.0, -step.toDouble())
				}
				else -> throw IllegalStateException()
			}

			positions.add(BlockPos(voxelX, voxelY, voxelZ))
			normals.add(normal)
		}

		return positions to normals
	}
}