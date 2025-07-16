package miyucomics.hexical.features.media_log

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.PatternIota
import net.minecraft.server.network.ServerPlayerEntity

class MediaLogComponent(val env: PlayerBasedCastEnv) : CastingEnvironmentComponent.PostExecution {
	override fun getKey() = MediaLogKey()
	class MediaLogKey : CastingEnvironmentComponent.Key<CastingEnvironmentComponent.PostExecution>

	override fun onPostExecution(result: CastResult) {
		val iota = result.cast
		if (iota is PatternIota)
			(env.castingEntity as ServerPlayerEntity).getMediaLog().pushPattern(iota.pattern)
	}
}