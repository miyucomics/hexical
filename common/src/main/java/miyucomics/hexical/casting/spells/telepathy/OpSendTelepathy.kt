package miyucomics.hexical.casting.spells.telepathy

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota

class OpSendTelepathy : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		ctx.caster.sendMessage(args[0].display(), true)
		return listOf()
	}
}