package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.state.PersistentStateHandler
import miyucomics.hexical.utils.CastingUtils

class OpSetArchLampStorage : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (!hasActiveArchLamp(env.caster))
			throw NeedsActiveArchLampMishap()
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env.caster)
		return SpellAction.Result(Spell(iota), 0, listOf())
	}

	private data class Spell(val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			PersistentStateHandler.getPlayerArchLampData(env.caster).storage = HexIotaTypes.serialize(iota)
		}
	}
}