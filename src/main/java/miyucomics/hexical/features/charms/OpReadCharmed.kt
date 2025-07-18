package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.features.charms.CharmCastEnv

class OpReadCharmed : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is CharmCastEnv)
			throw NeedsCharmedItemMishap()
		return listOf(env.getInternalStorage())
	}
}