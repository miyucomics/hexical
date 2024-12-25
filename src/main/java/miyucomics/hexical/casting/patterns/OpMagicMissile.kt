package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.entities.MagicMissileEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpMagicMissile : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val headOffset = args.getVec3(0, argc)
		val straightAxis = env.caster.rotationVector

		val upPitch = (-env.caster.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -env.caster.headYaw * (Math.PI.toFloat() / 180)
		val h = MathHelper.cos(yaw).toDouble()
		val j = MathHelper.cos(upPitch).toDouble()
		val upAxis = Vec3d(
			MathHelper.sin(yaw).toDouble() * j,
			MathHelper.sin(upPitch).toDouble(),
			h * j
		)

		val sideAxis = straightAxis.crossProduct(upAxis).normalize()
		val worldCoords = env.caster.eyePos
			.add(sideAxis.multiply(headOffset.x))
			.add(upAxis.multiply(headOffset.y))
			.add(straightAxis.multiply(headOffset.z))
		env.assertVecInRange(worldCoords)

		val velocity = args.getVec3(1, argc)
		return SpellAction.Result(Spell(worldCoords, velocity), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val missile = MagicMissileEntity(env.world)
			missile.setPos(position.x, position.y, position.z)
			missile.owner = env.caster
			env.world.spawnEntity(missile)
			missile.tick()
			missile.setVelocity(velocity.x, velocity.y, velocity.z)
		}
	}
}