package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.inits.HexicalItems

class OpGetArchLampMedia : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!hasActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		for (stack in ctx.caster.inventory.main)
			if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
				return ((stack.item as ArchLampItem).getMedia(stack).toDouble() / MediaConstants.DUST_UNIT).asActionResult
		if (ctx.caster.offHandStack.item == HexicalItems.ARCH_LAMP_ITEM && ctx.caster.offHandStack.orCreateNbt.getBoolean("active"))
			return ((ctx.caster.offHandStack.item as ArchLampItem).getMedia(ctx.caster.offHandStack).toDouble() / MediaConstants.DUST_UNIT).asActionResult
		return listOf(NullIota())
	}
}