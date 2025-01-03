package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.abs
import kotlin.math.floor

object DDAUtils {
	fun raycastBlock(position: Vec3d, direction: Vec3d, hitTarget: (BlockPos) -> Boolean, exceededBounds: (BlockPos) -> Boolean): List<Iota> {
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

		while (true) {
			val (axis, step, offset) = when {
				sideDistanceX < sideDistanceY && sideDistanceX < sideDistanceZ -> Triple('X', stepX, delta.x)
				sideDistanceY < sideDistanceZ -> Triple('Y', stepY, delta.y)
				else -> Triple('Z', stepZ, delta.z)
			}

			when (axis) {
				'X' -> {
					voxelX += step
					sideDistanceX += offset
				}

				'Y' -> {
					voxelY += step
					sideDistanceY += offset
				}

				'Z' -> {
					voxelZ += step
					sideDistanceZ += offset
				}

				else -> throw IllegalStateException()
			}

			if (hitTarget(BlockPos(voxelX, voxelY, voxelZ)))
				return Vec3d(voxelX + 0.5, voxelY + 0.5, voxelZ + 0.5).asActionResult
			if (exceededBounds(BlockPos(voxelX, voxelY, voxelZ)))
				return listOf(NullIota())
		}
	}

	fun raycastNormal(position: Vec3d, direction: Vec3d, hitTarget: (BlockPos) -> Boolean, exceededBounds: (BlockPos) -> Boolean): List<Iota> {
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

		var normal: Vec3d

		while (true) {
			val (axis, step, offset) = when {
				sideDistanceX < sideDistanceY && sideDistanceX < sideDistanceZ -> Triple('X', stepX, delta.x)
				sideDistanceY < sideDistanceZ -> Triple('Y', stepY, delta.y)
				else -> Triple('Z', stepZ, delta.z)
			}

			when (axis) {
				'X' -> {
					voxelX += step
					sideDistanceX += offset
					normal = Vec3d(-step.toDouble(), 0.0, 0.0)
				}

				'Y' -> {
					voxelY += step
					sideDistanceY += offset
					normal = Vec3d(0.0, -step.toDouble(), 0.0)
				}

				'Z' -> {
					voxelZ += step
					sideDistanceZ += offset
					normal = Vec3d(0.0, 0.0, -step.toDouble())
				}

				else -> throw IllegalStateException()
			}

			if (hitTarget(BlockPos(voxelX, voxelY, voxelZ)))
				return normal.asActionResult
			if (exceededBounds(BlockPos(voxelX, voxelY, voxelZ)))
				return listOf(NullIota())
		}
	}
}