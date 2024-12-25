package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota

class OpGetFinale : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val minterface = env as CastingEnvironmentMinterface
		if (minterface.getSpecializedSource() == SpecializedSource.HAND_LAMP || minterface.getSpecializedSource() == SpecializedSource.ARCH_LAMP)
			return minterface.getFinale().asActionResult
		return listOf(NullIota())
	}
}