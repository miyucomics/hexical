package miyucomics.hexical.casting.patterns.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

class OpSorobanIncrement : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		val sorobanValue = image.userData.getInt("soroban").toDouble()
		val newImage = image.withUsedOp().copy(stack = stack)
		stack.add(DoubleIota(sorobanValue))
		newImage.userData.putDouble("soroban", sorobanValue + 1)
		return OperationResult(newImage, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}