package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.state.ArchLampData
import miyucomics.hexical.state.PersistentStateHandler

class OpGetArchLampData(private val process: (CastingContext, ArchLampData) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!hasActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		return process(ctx, PersistentStateHandler.getPlayerArchLampData(ctx.caster))
	}
}