package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getInt
import at.petrak.hexcasting.api.spell.getPattern
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.EulerPathFinder

class OpShufflePattern : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingEnvironment) = EulerPathFinder.findAltDrawing(args.getPattern(0, argc), args.getInt(1, argc).toLong()).asActionResult
}