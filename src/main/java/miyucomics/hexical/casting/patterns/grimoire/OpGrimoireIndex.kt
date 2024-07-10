package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.registry.HexicalItems

class OpGrimoireIndex : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = ctx.caster.getStackInHand(ctx.otherHand)
		if (!stack.isOf(HexicalItems.GRIMOIRE_ITEM))
			throw MishapBadOffhandItem.of(stack, ctx.otherHand, "grimoire")
		return GrimoireItem.getPatternsInGrimoire(stack).asActionResult
	}
}