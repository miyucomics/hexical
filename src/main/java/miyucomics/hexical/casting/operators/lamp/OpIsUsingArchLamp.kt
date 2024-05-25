package miyucomics.hexical.casting.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.items.hasActiveArchLamp

class OpIsUsingArchLamp : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext) = listOf(BooleanIota(hasActiveArchLamp(args.getPlayer(0, argc))))
}