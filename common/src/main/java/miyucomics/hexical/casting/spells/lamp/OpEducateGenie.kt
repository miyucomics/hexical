package miyucomics.hexical.casting.spells.lamp

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.items.LampItem
import miyucomics.hexical.registry.HexicalAdvancements
import net.minecraft.item.ItemStack

class OpEducateGenie : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val patterns = args.getList(0, argc).toList()
		val (stack, hand) = ctx.getHeldItemToOperateOn { IXplatAbstractions.INSTANCE.findHexHolder(it) != null }
		if (stack.isEmpty)
			throw MishapBadOffhandItem.of(stack, hand, "lamp_full")
		if (!(stack.item is ArchLampItem || stack.item is LampItem))
			throw MishapBadOffhandItem.of(stack, hand, "lamp_full")
		return Triple(Spell(patterns, stack), 0, listOf())
	}

	private data class Spell(val patterns: List<Iota>, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			IXplatAbstractions.INSTANCE.findHexHolder(stack)?.writeHex(patterns, IXplatAbstractions.INSTANCE.findMediaHolder(stack)?.media!!)
			HexicalAdvancements.EDUCATE_GENIE.trigger(ctx.caster)
		}
	}
}