package miyucomics.hexical.casting.components

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent.PostExecution

class ScopeComponent(val env: CastingEnvironment) : PostExecution {
	override fun getKey() = ScopeKey()
	class ScopeKey : CastingEnvironmentComponent.Key<PostExecution> {}

	override fun onPostExecution(result: CastResult) {
		env.printMessage(result.cast.display())
	}
}