package miyucomics.hexical.casting.patterns.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.persistent_state.PersistentStateHandler

class OpIsUsingMasterLamp : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val state = PersistentStateHandler.getPlayerState(args.getPlayer(0, argc))
		return listOf(BooleanIota(state.active))
	}
}