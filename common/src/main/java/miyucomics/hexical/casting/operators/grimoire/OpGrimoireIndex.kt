package miyucomics.hexical.casting.operators.grimoire

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import miyucomics.hexical.items.GrimoireItem

class OpGrimoireIndex : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val (stack, _) = ctx.getHeldItemToOperateOn { it.item is GrimoireItem }
		return listOf(ListIota(GrimoireItem.getPatternsInGrimoire(stack)))
	}
}