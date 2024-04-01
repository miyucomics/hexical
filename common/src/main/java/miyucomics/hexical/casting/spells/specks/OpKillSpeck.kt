package miyucomics.hexical.casting.spells.specks

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.SpeckEntity

class OpKillSpeck : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		ctx.assertEntityInRange(entity)
		if (entity !is SpeckEntity)
			throw MishapBadEntity.of(entity, "speck")
		return Triple(Spell(entity), 0, listOf())
	}

	private data class Spell(val speck: SpeckEntity) : RenderedSpell {
		override fun cast(ctx: CastingContext) = speck.kill()
	}
}