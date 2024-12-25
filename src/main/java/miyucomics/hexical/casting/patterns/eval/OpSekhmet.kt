package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

class OpSekhmet(private val perserve: Int) : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.size < perserve)
			throw MishapNotEnoughArgs(perserve, stack.size)
		return OperationResult(image.withUsedOp().copy(stack = stack.takeLast(perserve)), listOf(), continuation, HexEvalSounds.NOTHING)
	}
}