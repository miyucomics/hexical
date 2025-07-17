package miyucomics.hexical.features.pattern_manipulation

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexCoord

class OpSimilarPattern : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val a = args.getPattern(0, argc).positions().windowed(2, 1)
		val b = args.getPattern(1, argc).positions().windowed(2, 1)
		return (normalize(a) == normalize(b)).asActionResult
	}

	companion object {
		fun normalize(lists: List<List<HexCoord>>) = lists.map { it.toSet() }.toSet()
	}
}