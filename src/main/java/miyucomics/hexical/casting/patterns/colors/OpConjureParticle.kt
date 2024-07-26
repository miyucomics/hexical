package miyucomics.hexical.casting.patterns.colors

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.particles.ConjureParticleOptions
import net.minecraft.util.math.Vec3d

class OpConjureParticle : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pos = args.getVec3(0, argc)
		ctx.assertVecInRange(pos)
		val color = args.getVec3(1, argc)
		val velocity = args.getVec3(2, argc)
		return Triple(Spell(pos, color.multiply(255.0), velocity), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d, val color: Vec3d, val velocity: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			ctx.world.addParticle(
				ConjureParticleOptions(color.x.toInt() shl 16 or color.y.toInt() shl 8 or color.z.toInt(), false),
				position.x, position.y, position.z,
				velocity.x, velocity.y, velocity.z
			)
		}
	}
}