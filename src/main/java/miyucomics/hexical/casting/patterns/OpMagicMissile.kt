package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.entities.MagicMissileEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpMagicMissile : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is PlayerBasedCastEnv)
			throw MishapBadCaster()
		val caster = env.castingEntity as ServerPlayerEntity

		val headOffset = args.getVec3(0, argc)
		val straightAxis = caster.rotationVector

		val upPitch = (-caster.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -caster.headYaw * (Math.PI.toFloat() / 180)
		val h = MathHelper.cos(yaw).toDouble()
		val j = MathHelper.cos(upPitch).toDouble()
		val upAxis = Vec3d(
			MathHelper.sin(yaw).toDouble() * j,
			MathHelper.sin(upPitch).toDouble(),
			h * j
		)

		val sideAxis = straightAxis.crossProduct(upAxis).normalize()
		val worldCoords = caster.eyePos
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
			missile.owner = env.castingEntity
			env.world.spawnEntity(missile)
			missile.tick()
			missile.setVelocity(velocity.x, velocity.y, velocity.z)
		}
	}
}