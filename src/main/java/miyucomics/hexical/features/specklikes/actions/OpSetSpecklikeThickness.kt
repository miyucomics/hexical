package miyucomics.hexical.features.specklikes.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.specklikes.FigureSpecklike

object OpSetSpecklikeThickness : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val specklike = args.getEntity(0, argc)
		if (specklike !is FigureSpecklike)
			throw MishapBadEntity.of(specklike, "specklike")
		val thickness = args.getPositiveDoubleUnderInclusive(1, 10.0, argc).toFloat()
		return SpellAction.Result(Spell(specklike, thickness), 0, listOf())
	}

	private data class Spell(val specklike: FigureSpecklike, val thickness: Float) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			specklike.setThickness(thickness)
		}
	}
}