package miyucomics.hexical.casting.patterns.tchotchke

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.environments.TchotchkeCastEnv
import miyucomics.hexical.casting.mishaps.NoTchotchkeMishap

class OpReadTchotchke : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is TchotchkeCastEnv)
			throw NoTchotchkeMishap()
		return listOf(env.getInternalStorage())
	}
}