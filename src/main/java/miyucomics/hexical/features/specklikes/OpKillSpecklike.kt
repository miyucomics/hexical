package miyucomics.hexical.features.specklikes

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity

object OpKillSpecklike : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val speck = args.getEntity(0, argc)
		if (speck !is BaseSpecklike)
			throw MishapBadEntity.of(speck, "speck")
		return SpellAction.Result(Spell(speck), 0, listOf())
	}

	private data class Spell(val speck: BaseSpecklike) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			speck.kill()
		}
	}
}