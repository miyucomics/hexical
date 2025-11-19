package miyucomics.hexical.features.specklikes.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.specklikes.BaseSpecklike

object OpSetSpecklikeLifespan : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val specklike = args.getEntity(0, argc)
		if (specklike !is BaseSpecklike)
			throw MishapBadEntity.of(specklike, "specklike")
		val lifespan = args.getPositiveInt(1, argc)
		return SpellAction.Result(Spell(specklike, lifespan), 0, listOf())
	}

	private data class Spell(val specklike: BaseSpecklike, val lifespan: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			specklike.setLifespan(lifespan)
		}
	}
}