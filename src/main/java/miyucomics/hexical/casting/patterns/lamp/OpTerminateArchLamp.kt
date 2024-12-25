package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.utils.CastingUtils

class OpTerminateArchLamp : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		if (!hasActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		return Triple(Spell(), 0, listOf())
	}

	private class Spell : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			val lamp = CastingUtils.getActiveArchLamp(ctx.caster)!!
			lamp.orCreateNbt.putBoolean("active", false)
			CastingUtils.castSpecial(ctx.world, ctx.caster, (lamp.item as ArchLampItem).getHex(lamp, ctx.world)!!, SpecializedSource.ARCH_LAMP, finale = true)
		}
	}
}