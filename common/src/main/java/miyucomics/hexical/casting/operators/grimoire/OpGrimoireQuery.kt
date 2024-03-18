package miyucomics.hexical.casting.operators.grimoire

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPattern
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.items.GrimoireItem

class OpGrimoireQuery : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val (stack, _) = ctx.getHeldItemToOperateOn { it.item is GrimoireItem }
		val uses = GrimoireItem.getUses(stack, args.getPattern(0, argc)) ?: return listOf(NullIota())
		return listOf(DoubleIota(uses.toDouble()))
	}
}