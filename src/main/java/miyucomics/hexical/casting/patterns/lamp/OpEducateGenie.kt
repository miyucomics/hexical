package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.interfaces.GenieLamp
import net.minecraft.item.ItemStack

class OpEducateGenie : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val patterns = args.getList(0, argc).toList()
		val stack = env.caster.getStackInHand(env.otherHand)
		if (stack.item !is GenieLamp)
			throw MishapBadOffhandItem.of(stack, "lamp_full")
		return SpellAction.Result(Spell(patterns, stack), 0, listOf())
	}

	private data class Spell(val patterns: List<Iota>, val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			IXplatAbstractions.INSTANCE.findHexHolder(stack)?.writeHex(patterns, null, IXplatAbstractions.INSTANCE.findMediaHolder(stack)?.media!!)
			HexicalAdvancements.EDUCATE_GENIE.trigger(env.caster)
		}
	}
}