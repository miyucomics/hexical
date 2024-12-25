package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpGreaterBlink : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val providedOffset = args.getVec3(0, argc)
		val straightAxis = ctx.caster.rotationVector

		val upPitch = (-ctx.caster.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -ctx.caster.headYaw * (Math.PI.toFloat() / 180)
		val h = MathHelper.cos(yaw).toDouble()
		val j = MathHelper.cos(upPitch).toDouble()
		val upAxis = Vec3d(MathHelper.sin(yaw).toDouble() * j, MathHelper.sin(upPitch).toDouble(), h * j)

		val sideAxis = straightAxis.crossProduct(upAxis).normalize()
		val worldOffset = Vec3d.ZERO
			.add(sideAxis.multiply(providedOffset.x))
			.add(upAxis.multiply(providedOffset.y))
			.add(straightAxis.multiply(providedOffset.z))

		if (worldOffset.length() > 128)
			throw MishapLocationTooFarAway(ctx.caster.eyePos.add(worldOffset))
		return Triple(Spell(ctx.caster.eyePos.add(worldOffset)), MediaConstants.DUST_UNIT * 2, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			ctx.caster.teleport(position.x, position.y, position.z)
		}
	}
}