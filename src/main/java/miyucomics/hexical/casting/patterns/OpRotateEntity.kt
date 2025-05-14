package miyucomics.hexical.casting.patterns

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

class OpRotateEntity : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val target = args.getEntity(0, argc)
		val yaw = args.getDouble(1, argc)
		val pitch = args.getDouble(2, argc)
		var cost = MediaConstants.DUST_UNIT / 2
		if (target is PlayerEntity)
			cost = MediaConstants.CRYSTAL_UNIT
		if (target == env.castingEntity)
			cost = 0
		return SpellAction.Result(Spell(target, yaw, pitch), cost, listOf(ParticleSpray.burst(target.eyePos, 1.0)))
	}

	private data class Spell(val target: Entity, val yaw: Double, val pitch: Double) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			target.prevPitch = target.pitch
			target.prevYaw = target.yaw
			target.bodyYaw += (yaw * 360).toFloat()
			target.yaw += (yaw * 360).toFloat()
			target.headYaw += (yaw * 360).toFloat()
			target.pitch += (pitch * 360).toFloat()
		}
	}
}