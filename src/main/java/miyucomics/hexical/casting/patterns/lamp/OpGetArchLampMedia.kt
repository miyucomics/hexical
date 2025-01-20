package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.casting.mishaps.NeedsArchGenieLamp
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.items.hasActiveArchLamp
import net.minecraft.server.network.ServerPlayerEntity

class OpGetArchLampMedia : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			return listOf(NullIota())
		if (!hasActiveArchLamp(caster))
			throw NeedsArchGenieLamp()
		for (stack in caster.inventory.main)
			if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
				return ((stack.item as ArchLampItem).getMedia(stack).toDouble() / MediaConstants.DUST_UNIT).asActionResult
		if (caster.offHandStack.item == HexicalItems.ARCH_LAMP_ITEM && caster.offHandStack.orCreateNbt.getBoolean("active"))
			return ((caster.offHandStack.item as ArchLampItem).getMedia(caster.offHandStack).toDouble() / MediaConstants.DUST_UNIT).asActionResult
		return listOf(NullIota())
	}
}