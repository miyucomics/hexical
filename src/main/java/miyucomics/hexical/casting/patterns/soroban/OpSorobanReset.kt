package miyucomics.hexical.casting.patterns.soroban

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota

class OpSorobanReset : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		(env as CastingEnvironmentMinterface).resetSoroban()
		return listOf()
	}
}