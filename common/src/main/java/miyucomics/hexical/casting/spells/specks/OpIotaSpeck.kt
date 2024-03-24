package miyucomics.hexical.casting.spells.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.SpeckEntity

class OpIotaSpeck : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		ctx.assertEntityInRange(entity)
		val iota = args[1]
		if (entity !is SpeckEntity)
			throw MishapBadEntity.of(entity, "speck")
		return Triple(Spell(entity, iota), 0, listOf())
	}

	private data class Spell(val speck: SpeckEntity, val iota: Iota) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			if (iota.type == PatternIota.TYPE)
				speck.setPattern((iota as PatternIota).pattern)
			else
				speck.setLabel(iota.display().string)
		}
	}
}