package miyucomics.hexical.casting.actions.pattern_manipulation

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota

class OpDeserializePattern : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val input = args.getList(0, argc)
		if (input.size() == 0)
			throw MishapInvalidIota.of(args[0], 0, "number_list")
		val strokes = input.map { element ->
			if (element !is DoubleIota)
				throw MishapInvalidIota.of(args[0], 0, "number_list")
			val value = element.double.toInt().mod(HexDir.values().size)
			HexDir.values()[value]
		}
		return HexPattern(strokes[0], strokes.asSequence().windowed(2).map { (a, b) -> b.angleFrom(a) }.toMutableList()).asActionResult
	}
}