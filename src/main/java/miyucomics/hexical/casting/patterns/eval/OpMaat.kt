package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBool
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.mishaps.AssertMishap

class OpMaat : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (!args.getBool(0, argc))
			throw AssertMishap(args[1])
		return listOf()
	}
}