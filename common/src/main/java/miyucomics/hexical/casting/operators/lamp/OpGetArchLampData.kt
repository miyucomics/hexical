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
		return listOf(
			when (mode) {
				0 -> Vec3Iota(state.position)
				1 -> Vec3Iota(state.rotation)
				2 -> Vec3Iota(state.velocity)
				3 -> DoubleIota((ctx.world.time - (state.time + 1)).toDouble())
				4 -> HexIotaTypes.deserialize(state.storage, ctx.world)
				else -> NullIota()
			}
		)
	}
}