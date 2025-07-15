package miyucomics.hexical.casting.actions.lamp

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.environments.LampCastEnv
import miyucomics.hexical.casting.mishaps.NeedsLampMishap

class OpGetFinale : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is LampCastEnv)
			throw NeedsLampMishap()
		return env.getFinale().asActionResult
	}
}