package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.state.PersistentStateHandler

class OpGetArchLampData(private val mode: Int) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!hasActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		val state = PersistentStateHandler.getPlayerArchLampData(ctx.caster)
		return when (mode) {
			0 -> state.position.asActionResult
			1 -> state.rotation.asActionResult
			2 -> state.velocity.asActionResult
			3 -> (ctx.world.time - (state.time + 1)).asActionResult
			4 -> listOf(HexIotaTypes.deserialize(state.storage, ctx.world))
			else -> throw IllegalStateException()
		}
	}
}