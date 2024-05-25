package miyucomics.hexical.casting.operators.grimoire

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPattern
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.registry.HexicalItems

class OpGrimoireQuery : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val (stack, hand) = ctx.getHeldItemToOperateOn { it.isOf(HexicalItems.GRIMOIRE_ITEM) }
		if (stack.item !is GrimoireItem)
			throw MishapBadOffhandItem.of(stack, hand, "grimoire")
		val uses = GrimoireItem.getUses(stack, args.getPattern(0, argc)) ?: return listOf(NullIota())
		return listOf(DoubleIota(uses.toDouble()))
	}
}