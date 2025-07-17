package miyucomics.hexical.features.rotate

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.MathHelper

class OpRotateEntity : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val target = args.getEntity(0, argc)
		val yaw = args.getDouble(1, argc) * MathHelper.DEGREES_PER_RADIAN
		val pitch = args.getDouble(2, argc) * MathHelper.DEGREES_PER_RADIAN
		var cost = MediaConstants.DUST_UNIT / 2
		if (target is PlayerEntity)
			cost = MediaConstants.CRYSTAL_UNIT
		if (target == env.castingEntity)
			cost = 0
		return SpellAction.Result(Spell(target, yaw.toFloat(), pitch.toFloat()), cost, listOf(ParticleSpray.burst(target.eyePos, 1.0)))
	}

	private data class Spell(val target: Entity, val yaw: Float, val pitch: Float) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			target.prevPitch = target.pitch
			target.prevYaw = target.yaw
			target.bodyYaw += yaw
			target.yaw += yaw
			target.headYaw += yaw
			target.pitch += pitch
			if (target is ServerPlayerEntity)
				target.networkHandler.requestTeleport(target.x, target.y, target.z, target.yaw, target.pitch)
		}
	}
}