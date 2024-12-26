package miyucomics.hexical.casting.patterns.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

class OpSorobanReset : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val newImage = image.withUsedOp().copy()
		newImage.userData.putDouble("soroban", 0.0)
		return OperationResult(newImage, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}