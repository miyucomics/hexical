package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPlayer
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.utils.CastingUtils

class OpEnlightened : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val player = args.getPlayer(0, argc)
		env.assertEntityInRange(player)
		return CastingUtils.isEnlightened(player).asActionResult
	}
}