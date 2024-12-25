package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota

class OpSimilar : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingEnvironment) = (args[0].type == args[1].type).asActionResult
}