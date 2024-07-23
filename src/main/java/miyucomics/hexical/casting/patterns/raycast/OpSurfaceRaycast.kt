package miyucomics.hexical.casting.patterns.raycast

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import net.minecraft.block.Blocks
import net.minecraft.block.FluidBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.floor

class OpSurfaceRaycast : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val start = args.getVec3(0, argc)
		ctx.assertVecInRange(start)
		val direction = args.getVec3(1, argc).normalize()
		if (direction == Vec3d.ZERO)
			return listOf(NullIota())
		val delta = Vec3d(abs(1 / direction.x), abs(1 / direction.y), abs(1 / direction.z))

		var voxelX = floor(start.x)
		var voxelY = floor(start.y)
		var voxelZ = floor(start.z)
		val stepX: Int
		val stepY: Int
		val stepZ: Int
		var sideDistanceX: Double
		var sideDistanceY: Double
		var sideDistanceZ: Double

		if (direction.x > 0) {
			stepX = 1
			sideDistanceX = (voxelX + 1 - start.x) * delta.x
		} else {
			stepX = -1
			sideDistanceX = (start.x - voxelX) * delta.x
		}

		if (direction.y > 0) {
			stepY = 1
			sideDistanceY = (voxelY + 1 - start.y) * delta.y
		} else {
			stepY = -1
			sideDistanceY = (start.y - voxelY) * delta.y
		}

		if (direction.z > 0) {
			stepZ = 1
			sideDistanceZ = (voxelZ + 1 - start.z) * delta.z
		} else {
			stepZ = -1
			sideDistanceZ = (start.z - voxelZ) * delta.z
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

			if (ctx.world.getBlockState(BlockPos(voxelX, voxelY, voxelZ)).block is FluidBlock)
				return normal.asActionResult
			if (!ctx.isVecInRange(Vec3d(voxelX, voxelY, voxelZ)))
				return listOf(NullIota())
		}
	}
}