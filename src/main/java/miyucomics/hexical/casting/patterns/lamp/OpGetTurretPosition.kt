package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.env.TurretLampCastEnv
import miyucomics.hexical.casting.mishaps.NoTurretMishap

class OpGetTurretPosition : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is TurretLampCastEnv)
			throw NoTurretMishap()
		return env.lamp.pos.asActionResult
	}
}