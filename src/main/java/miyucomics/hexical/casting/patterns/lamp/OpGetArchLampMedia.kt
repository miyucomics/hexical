package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.items.hasActiveArchLamp

class OpGetArchLampMedia : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (!hasActiveArchLamp(env.caster))
			throw NeedsActiveArchLampMishap()
		for (stack in env.caster.inventory.main)
			if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
				return ((stack.item as ArchLampItem).getMedia(stack).toDouble() / MediaConstants.DUST_UNIT).asActionResult
		if (env.caster.offHandStack.item == HexicalItems.ARCH_LAMP_ITEM && env.caster.offHandStack.orCreateNbt.getBoolean("active"))
			return ((env.caster.offHandStack.item as ArchLampItem).getMedia(env.caster.offHandStack).toDouble() / MediaConstants.DUST_UNIT).asActionResult
		return listOf(NullIota())
	}
}