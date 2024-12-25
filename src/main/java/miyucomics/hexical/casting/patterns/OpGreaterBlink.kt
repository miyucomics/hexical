package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpGreaterBlink : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val providedOffset = args.getVec3(0, argc)
		val straightAxis = env.caster.rotationVector

		val upPitch = (-env.caster.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -env.caster.headYaw * (Math.PI.toFloat() / 180)
		val h = MathHelper.cos(yaw).toDouble()
		val j = MathHelper.cos(upPitch).toDouble()
		val upAxis = Vec3d(MathHelper.sin(yaw).toDouble() * j, MathHelper.sin(upPitch).toDouble(), h * j)

		val sideAxis = straightAxis.crossProduct(upAxis).normalize()
		val worldOffset = Vec3d.ZERO
			.add(sideAxis.multiply(providedOffset.x))
			.add(upAxis.multiply(providedOffset.y))
			.add(straightAxis.multiply(providedOffset.z))

		if (worldOffset.length() > 128)
			throw MishapBadLocation(env.caster.eyePos.add(worldOffset))
		return SpellAction.Result(Spell(env.caster.eyePos.add(worldOffset)), MediaConstants.DUST_UNIT * 2, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.caster.teleport(position.x, position.y, position.z)
		}
	}
}