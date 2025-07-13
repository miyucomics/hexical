package miyucomics.hexical.casting.components

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent.PostExecution
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.PatternIota
import miyucomics.hexical.features.player_state.fields.getLedger
import net.minecraft.server.network.ServerPlayerEntity

class LedgerRecordComponent(val env: PlayerBasedCastEnv) : PostExecution {
	override fun getKey() = LedgerKey()
	class LedgerKey : CastingEnvironmentComponent.Key<PostExecution>

	override fun onPostExecution(result: CastResult) {
		val iota = result.cast
		if (iota is PatternIota)
			(env.castingEntity as ServerPlayerEntity).getLedger().pushPattern(iota.pattern)
	}
}