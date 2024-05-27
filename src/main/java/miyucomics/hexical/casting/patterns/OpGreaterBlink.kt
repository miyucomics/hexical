package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpGreaterBlink : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val headOffset = args.getVec3(0, argc)
		val xAxis = ctx.caster.rotationVector
		val upPitch = (-ctx.caster.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -ctx.caster.headYaw * (Math.PI.toFloat() / 180)
		val h = MathHelper.cos(yaw).toDouble()
		val j = MathHelper.cos(upPitch).toDouble()
		val yAxis = Vec3d(MathHelper.sin(yaw).toDouble() * j, MathHelper.sin(upPitch).toDouble(), h * j)
		val zAxis = xAxis.crossProduct(yAxis).normalize()
		val offset = Vec3d.ZERO
			.add(xAxis.multiply(headOffset.x))
			.add(yAxis.multiply(headOffset.y))
			.add(zAxis.multiply(headOffset.z))
		if (offset.length() > 128)
			throw MishapLocationTooFarAway(ctx.caster.eyePos.add(offset))
		return Triple(Spell(ctx.caster.eyePos.add(offset)), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			ctx.caster.teleport(position.x, position.y, position.z)
		}
	}
}