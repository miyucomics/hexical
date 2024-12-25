package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.items.hasActiveArchLamp

class OpIsUsingArchLamp : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment) = hasActiveArchLamp(args.getPlayer(0, argc)).asActionResult
}