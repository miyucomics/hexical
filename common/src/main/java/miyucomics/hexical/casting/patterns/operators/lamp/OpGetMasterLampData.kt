package miyucomics.hexical.casting.patterns.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.MishapNeedActiveMasterLamp
import miyucomics.hexical.persistent_state.PersistentStateHandler

class OpGetMasterLampData(private val mode: Int) : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val state = PersistentStateHandler.getPlayerState(ctx.caster)
		if (!state.active)
			throw MishapNeedActiveMasterLamp()
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