package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.items.GrimoireItem

class OpGrimoireIndex : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = env.caster.getStackInHand(env.otherHand)
		if (!stack.isOf(HexicalItems.GRIMOIRE_ITEM))
			throw MishapBadOffhandItem.of(stack, env.otherHand, "grimoire")
		return GrimoireItem.getPatternsInGrimoire(stack).asActionResult
	}
}