package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpGreaterBlink : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val caster = env.castingEntity ?: throw MishapBadCaster()

		val providedOffset = args.getVec3(0, argc)
		val straightAxis = caster.rotationVector

		val upPitch = (-caster.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -caster.headYaw * (Math.PI.toFloat() / 180)
		val h = MathHelper.cos(yaw).toDouble()
		val j = MathHelper.cos(upPitch).toDouble()
		val upAxis = Vec3d(MathHelper.sin(yaw).toDouble() * j, MathHelper.sin(upPitch).toDouble(), h * j)

		val sideAxis = straightAxis.crossProduct(upAxis).normalize()
		val worldOffset = Vec3d.ZERO
			.add(sideAxis.multiply(providedOffset.x))
			.add(upAxis.multiply(providedOffset.y))
			.add(straightAxis.multiply(providedOffset.z))

		val destination = caster.pos.add(worldOffset)
		if (worldOffset.length() > 64)
			throw MishapBadLocation(destination)
		return SpellAction.Result(Spell(destination), MediaConstants.DUST_UNIT, listOf(ParticleSpray.cloud(destination, 1.0)))
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.castingEntity!!.teleport(position.x, position.y, position.z)
		}
	}
}