package miyucomics.hexical.casting.actions.charms

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.environments.CurioCastEnv
import miyucomics.hexical.casting.mishaps.NeedsCharmedItemMishap
import miyucomics.hexical.misc.CastingUtils

class OpWriteCharmed : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is CurioCastEnv)
			throw NeedsCharmedItemMishap()
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env)
		env.setInternalStorage(iota)
		return listOf()
	}
}