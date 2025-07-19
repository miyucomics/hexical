package miyucomics.hexical.features.lamps

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota

object OpSetHandLampStorage : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is HandLampCastEnv)
			throw NeedsHandLampMishap()
		return SpellAction.Result(Spell(args[0]), 0, listOf())
	}

	private data class Spell(val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			(env as HandLampCastEnv).setInternalIota(iota)
		}
	}
}