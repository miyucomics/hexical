package miyucomics.hexical.casting.spells.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.SpeckEntity

class OpZRotationSpeck : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		if (entity !is SpeckEntity)
			throw MishapBadEntity.of(entity, "speck")
		ctx.assertEntityInRange(entity)
		val rotation = args.getDoubleBetween(1, 0.0, 1.0, argc)
		return Triple(Spell(entity, rotation), 0, listOf())
	}

	private data class Spell(val speck: SpeckEntity, val zRotation: Double) : RenderedSpell {
		override fun cast(ctx: CastingContext) = speck.setZRotation(zRotation.toFloat() * 360f)
	}
}