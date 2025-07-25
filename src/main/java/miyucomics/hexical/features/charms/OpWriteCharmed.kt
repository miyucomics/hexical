package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.misc.CastingUtils

object OpWriteCharmed : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is CharmCastEnv)
			throw NeedsCharmedItemMishap()
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env)
		env.setInternalStorage(iota)
		return emptyList()
	}
}