package miyucomics.hexical.features.specklikes.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.features.pigments.getPigment
import miyucomics.hexical.features.specklikes.FigureSpecklike

object OpSetSpecklikePigment : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val specklike = args.getEntity(0, argc)
		if (specklike !is FigureSpecklike)
			throw MishapBadEntity.of(specklike, "specklike")
		val pigment = args.getPigment(1, argc)
		return SpellAction.Result(Spell(specklike, pigment), 0, listOf())
	}

	private data class Spell(val specklike: FigureSpecklike, val pigment: FrozenPigment) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			specklike.setPigment(pigment)
		}
	}
}