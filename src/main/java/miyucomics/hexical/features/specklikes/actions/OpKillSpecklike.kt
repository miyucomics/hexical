package miyucomics.hexical.features.specklikes.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.specklikes.BaseSpecklike

object OpKillSpecklike : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val specklike = args.getEntity(0, argc)
		if (specklike !is BaseSpecklike)
			throw MishapBadEntity.of(specklike, "specklike")
		return SpellAction.Result(Spell(specklike), 0, listOf())
	}

	private data class Spell(val specklike: BaseSpecklike) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			specklike.kill()
		}
	}
}