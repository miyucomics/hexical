package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.casting.environments.TurretLampCastEnv
import miyucomics.hexical.casting.mishaps.MagicMissileMishap
import miyucomics.hexical.entities.MagicMissileEntity
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.abs

class OpMagicMissile : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = parseLocation(env, args.getVec3(0, argc))
		env.assertVecInRange(position)
		return SpellAction.Result(Spell(position, args.getVec3(1, argc)), MediaConstants.DUST_UNIT, listOf(ParticleSpray.cloud(position, 1.0)))
	}

	private fun parseLocation(env: CastingEnvironment, relative: Vec3d): Vec3d {
		if (env.castingEntity != null) {
			val caster = env.castingEntity!!
			val straightAxis = caster.rotationVector
			val upPitch = (-caster.pitch + 90) * (Math.PI.toFloat() / 180)
			val yaw = -caster.headYaw * (Math.PI.toFloat() / 180)
			val j = MathHelper.cos(upPitch).toDouble()
			val upAxis = Vec3d(MathHelper.sin(yaw).toDouble() * j, MathHelper.sin(upPitch).toDouble(), MathHelper.cos(yaw).toDouble() * j)
			return caster.eyePos
				.add(straightAxis.crossProduct(upAxis).normalize().multiply(relative.x))
				.add(upAxis.multiply(relative.y))
				.add(straightAxis.multiply(relative.z))
		}

		if (env !is TurretLampCastEnv)
			throw MagicMissileMishap()

		val straightAxis = Vec3d.of(env.normal)
		val upAxis = Vec3d.of(if (abs(straightAxis.dotProduct(Vec3d(0.0, 1.0, 0.0))) > 0.9)
			Direction.NORTH.vector
		else
			Direction.UP.vector)

		return env.lamp.pos
			.add(straightAxis.crossProduct(upAxis).normalize().multiply(relative.x))
			.add(upAxis.multiply(relative.y))
			.add(straightAxis.multiply(relative.z))
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val missile = MagicMissileEntity(env.world)
			missile.setPos(position.x, position.y, position.z)
			missile.owner = env.castingEntity
			env.world.spawnEntity(missile)
			missile.tick()
			missile.setVelocity(velocity.x, velocity.y, velocity.z)
		}
	}
}