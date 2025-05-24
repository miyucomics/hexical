package miyucomics.hexical.casting.patterns.tweaks

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.environments.TweakedItemCastEnv
import miyucomics.hexical.casting.mishaps.NoTchotchkeMishap
import miyucomics.hexical.utils.CastingUtils

class OpWriteTchotchke : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is TweakedItemCastEnv)
			throw NoTchotchkeMishap()
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env)
		env.setInternalStorage(iota)
		return listOf()
	}
}