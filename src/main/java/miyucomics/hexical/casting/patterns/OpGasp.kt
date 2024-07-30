package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.entity.Entity

class OpGasp : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		ctx.assertEntityInRange(target)
		return Triple(Spell(target), MediaConstants.DUST_UNIT, listOf(ParticleSpray.cloud(target.pos, 1.0)))
	}

	private data class Spell(val target: Entity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			target.air = target.maxAir
		}
	}
}