package miyucomics.hexical.casting.patterns.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import miyucomics.hexical.casting.mishaps.MishapNeedActiveMasterLamp
import miyucomics.hexical.persistent_state.StateHandler

class OpGetMasterLampData(private val mode: Int) : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val state = StateHandler.getPlayerState(ctx.caster)
		if (!state.active)
			throw MishapNeedActiveMasterLamp()
		when (mode) {
			0 -> return listOf(Vec3Iota(state.startPosition))
			1 -> return listOf(Vec3Iota(state.startRotation))
			2 -> return listOf(DoubleIota((ctx.world.time - state.startTime).toDouble()))
		}
		return listOf(NullIota())
	}
}