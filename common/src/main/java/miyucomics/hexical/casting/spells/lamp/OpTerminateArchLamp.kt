package miyucomics.hexical.casting.spells.lamp

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.registry.HexicalItems

class OpTerminateArchLamp : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		if (!hasActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		return Triple(Spell(), 0, listOf())
	}

	private class Spell : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			for (slot in ctx.caster.inventory.main)
				if (slot.item == HexicalItems.ARCH_LAMP_ITEM)
					slot.orCreateNbt.putBoolean("active", false)
		}
	}
}