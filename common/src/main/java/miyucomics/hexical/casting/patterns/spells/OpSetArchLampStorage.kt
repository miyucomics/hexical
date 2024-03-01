package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.persistent_state.PersistentStateHandler
import miyucomics.hexical.utils.CastingUtils

class OpSetArchLampStorage : SpellAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>>? {
		if (!CastingUtils.doesPlayerHaveActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		val iota = args[0]
		val trueName = MishapOthersName.getTrueNameFromDatum(iota, ctx.caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
		return Triple(Spell(iota), 0, listOf(ParticleSpray.burst(ctx.caster.pos, 1.0)))
	}

	private data class Spell(val iota: Iota) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val state = PersistentStateHandler.getPlayerState(ctx.caster)
			state.storage = HexIotaTypes.serialize(iota)
		}
	}
}