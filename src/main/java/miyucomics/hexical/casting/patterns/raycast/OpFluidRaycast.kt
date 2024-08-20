package miyucomics.hexical.casting.patterns.raycast

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import net.minecraft.block.FluidBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.abs
import kotlin.math.floor

class OpFluidRaycast : ConstMediaAction {
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

			if (ctx.world.getBlockState(BlockPos(voxelX, voxelY, voxelZ)).block is FluidBlock)
				return Vec3d(voxelX + 0.5, voxelY + 0.5, voxelZ + 0.5).asActionResult
			if (!ctx.isVecInRange(Vec3d(voxelX, voxelY, voxelZ)))
				return listOf(NullIota())
		}
	}
}