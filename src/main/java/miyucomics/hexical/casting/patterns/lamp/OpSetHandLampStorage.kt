package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.environments.HandLampCastEnv
import miyucomics.hexical.casting.mishaps.NoHandLampMishap

class OpSetHandLampStorage : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is HandLampCastEnv)
			throw NoHandLampMishap()
		return SpellAction.Result(Spell(args[0]), 0, listOf())
	}

	private data class Spell(val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			(env as HandLampCastEnv).setInternalIota(iota)
		}
	}
}