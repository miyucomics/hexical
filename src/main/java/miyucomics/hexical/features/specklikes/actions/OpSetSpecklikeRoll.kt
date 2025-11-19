package miyucomics.hexical.features.specklikes.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDoubleBetween
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.specklikes.BaseSpecklike

object OpSetSpecklikeRoll : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val specklike = args.getEntity(0, argc)
		if (specklike !is BaseSpecklike)
			throw MishapBadEntity.of(specklike, "specklike")
		val roll = args.getDoubleBetween(1, 0.0, 1.0, argc).toFloat() * 360
		return SpellAction.Result(Spell(specklike, roll), 0, listOf())
	}

	private data class Spell(val specklike: BaseSpecklike, val size: Float) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			specklike.setRoll(size)
		}
	}
}