package miyucomics.hexical.casting.patterns.charms

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.environments.CharmedItemCastEnv
import miyucomics.hexical.casting.mishaps.NotCharmedItemMishap

class OpReadCharmed : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is CharmedItemCastEnv)
			throw NotCharmedItemMishap()
		return listOf(env.getInternalStorage())
	}
}