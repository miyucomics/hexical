package miyucomics.hexical.casting.patterns.evocation

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.state.EvokeState

class OpIsEvoking : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val player = args.getPlayer(0, argc)
		return EvokeState.duration.getOrDefault(player.uuid, -1).asActionResult
	}
}