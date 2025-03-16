package miyucomics.hexical.casting.components

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent.PostExecution
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.PatternIota
import miyucomics.hexical.data.LedgerData
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.server.network.ServerPlayerEntity

class LedgerRecordComponent(val env: PlayerBasedCastEnv) : PostExecution {
	override fun getKey() = LedgerKey()
	class LedgerKey : CastingEnvironmentComponent.Key<PostExecution>

	override fun onPostExecution(result: CastResult) {
		val iota = result.cast
		if (iota is PatternIota)
			LedgerData.getLedger(env.castingEntity as ServerPlayerEntity).pushPattern(iota.pattern)
	}
}