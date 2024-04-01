package miyucomics.hexical.casting.operators.grimoire

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import miyucomics.hexical.items.GrimoireItem

class OpGrimoireIndex : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val (stack, hand) = ctx.getHeldItemToOperateOn { it.item is GrimoireItem }
		if (stack.item !is GrimoireItem)
			throw MishapBadOffhandItem.of(stack, hand, "grimoire")
		return listOf(ListIota(GrimoireItem.getPatternsInGrimoire(stack)))
	}
}