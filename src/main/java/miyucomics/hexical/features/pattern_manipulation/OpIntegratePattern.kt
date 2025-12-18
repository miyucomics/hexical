package miyucomics.hexical.features.pattern_manipulation

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota

object OpIntegratePattern : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val raw = args.getList(0, argc).toMutableList()
		if (raw.isEmpty())
			throw MishapInvalidIota.of(args[0], 0, "pattern_list")
		val segments = raw.map { segment ->
			if (segment !is PatternIota)
				throw MishapInvalidIota.of(args[0], 0, "pattern_list")
			segment.pattern
		}
		return segments.subList(1, segments.size).fold(segments[0], PatternArithmetic::addPatterns).asActionResult
	}
}