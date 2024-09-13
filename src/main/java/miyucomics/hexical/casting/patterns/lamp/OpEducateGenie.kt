package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.interfaces.GenieLamp
import miyucomics.hexical.inits.HexicalAdvancements
import net.minecraft.item.ItemStack

class OpEducateGenie : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val patterns = args.getList(0, argc).toList()
		val stack = ctx.caster.getStackInHand(ctx.otherHand)
		if (stack.item !is GenieLamp)
			throw MishapBadOffhandItem.of(stack, ctx.otherHand, "lamp_full")
		return Triple(Spell(patterns, stack), 0, listOf())
	}

	private data class Spell(val patterns: List<Iota>, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			IXplatAbstractions.INSTANCE.findHexHolder(stack)?.writeHex(patterns, IXplatAbstractions.INSTANCE.findMediaHolder(stack)?.media!!)
			HexicalAdvancements.EDUCATE_GENIE.trigger(ctx.caster)
		}
	}
}