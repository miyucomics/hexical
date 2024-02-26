package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedActiveArchLampMishap
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.CastingUtils

class OpTerminateLamp : SpellAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		if (!CastingUtils.doesPlayerHaveActiveArchLamp(ctx.caster))
			throw NeedActiveArchLampMishap()
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