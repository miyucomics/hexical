package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.casting.frames.SisyphusFrame

object OpSisyphus : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)
		val code = stack.getList(stack.lastIndex)
		stack.removeLast()

		if (code.nonEmpty) {
			val frame = SisyphusFrame(code)
			val newImage = image.withUsedOp().copy(stack = stack)
			return OperationResult(newImage, listOf(), continuation.pushFrame(frame), HexEvalSounds.THOTH)
		}

		return OperationResult(image, listOf(), continuation, HexEvalSounds.NOTHING)
	}
}