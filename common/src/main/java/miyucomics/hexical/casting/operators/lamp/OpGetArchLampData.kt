package miyucomics.hexical.casting.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.state.PersistentStateHandler

class OpGetArchLampData(private val mode: Int) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!hasActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		val state = PersistentStateHandler.getPlayerState(ctx.caster)
		when (mode) {
			0 -> return listOf(Vec3Iota(state.position))
			1 -> return listOf(Vec3Iota(state.rotation))
			2 -> return listOf(Vec3Iota(state.velocity))
			3 -> return listOf(DoubleIota((ctx.world.time - state.time).toDouble()))
			4 -> return listOf(HexIotaTypes.deserialize(state.storage, ctx.world))
		}
		return listOf(NullIota())
	}
}