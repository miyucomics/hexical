package miyucomics.hexical.casting.actions.pattern_manipulation

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota

class OpCongruentPattern : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val a = args.getPattern(0, argc)
		val b = args.getPattern(1, argc)
		return (a.anglesSignature() == b.anglesSignature() && a.startDir == b.startDir).asActionResult
	}
}