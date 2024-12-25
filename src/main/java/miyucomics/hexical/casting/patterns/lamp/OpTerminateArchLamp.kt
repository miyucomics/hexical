package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.utils.CastingUtils

class OpTerminateArchLamp : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (!hasActiveArchLamp(env.caster))
			throw NeedsActiveArchLampMishap()
		return SpellAction.Result(Spell(), 0, listOf())
	}

	private class Spell : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val lamp = CastingUtils.getActiveArchLamp(env.caster)!!
			lamp.orCreateNbt.putBoolean("active", false)
			CastingUtils.castSpecial(env.world, env.caster, (lamp.item as ArchLampItem).getHex(lamp, env.world)!!, SpecializedSource.ARCH_LAMP, finale = true)
		}
	}
}