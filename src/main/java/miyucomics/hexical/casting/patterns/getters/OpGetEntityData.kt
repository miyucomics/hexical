package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpGetEntityData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getEntity(0, argc)
		return when (mode) {
			0 -> entity.isOnFire.asActionResult
			1 -> (entity.fireTicks.toDouble() / 20).asActionResult
			2 -> entity.isWet.asActionResult
			3 -> entity.width.asActionResult
			4 -> {
				val upPitch = (-entity.pitch + 90) * (Math.PI.toFloat() / 180)
				val yaw = -entity.headYaw * (Math.PI.toFloat() / 180)
				val h = MathHelper.cos(yaw).toDouble()
				val j = MathHelper.cos(upPitch).toDouble()
				Vec3d(
					MathHelper.sin(yaw).toDouble() * j,
					MathHelper.sin(upPitch).toDouble(),
					h * j
				).asActionResult
			}
			else -> throw IllegalStateException()
		}
	}
}