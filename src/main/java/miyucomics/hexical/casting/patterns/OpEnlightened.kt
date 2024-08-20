package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.utils.CastingUtils

class OpEnlightened : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val player = args.getPlayer(0, argc)
		return CastingUtils.isEnlightened(player).asActionResult
	}
}