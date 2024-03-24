package miyucomics.hexical.casting.spells.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.SpeckEntity

class OpLifetimeSpeck : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		val lifespan = args.getInt(1, argc)
		ctx.assertEntityInRange(entity)
		if (entity !is SpeckEntity)
			throw MishapBadEntity.of(entity, "speck")
		return Triple(Spell(entity, lifespan), 0, listOf())
	}

	private data class Spell(val speck: SpeckEntity, val lifespan: Int) : RenderedSpell {
		override fun cast(ctx: CastingContext) = speck.setLifespan(lifespan)
	}
}