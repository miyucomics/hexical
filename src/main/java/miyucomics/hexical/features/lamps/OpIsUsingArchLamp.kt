package miyucomics.hexical.features.lamps

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPlayer
import at.petrak.hexcasting.api.casting.iota.Iota

class OpIsUsingArchLamp : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val player = args.getPlayer(0, argc)
		env.assertEntityInRange(player)
		return hasActiveArchLamp(player).asActionResult
	}
}