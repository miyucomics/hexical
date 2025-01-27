package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.eval.vm.FrameForEach
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.casting.mishaps.NeedsThothMishap

abstract class OpModifyThoth : Action {
	abstract fun updateFrame(frame: FrameForEach, stack: MutableList<Iota>): FrameForEach

	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val callStack = mutableListOf<ContinuationFrame>()
		var newContinuation = continuation
		val forEach: FrameForEach

		while (true) {
			if (newContinuation !is SpellContinuation.NotDone)
				throw NeedsThothMishap()

			val frame = newContinuation.frame
			newContinuation = newContinuation.next

			if (frame is FrameForEach) {
				forEach = frame
				break
			} else {
				callStack.add(frame)
			}
		}

		val newStack = image.stack.toMutableList()
		newContinuation = newContinuation.pushFrame(updateFrame(forEach, newStack))
		while (callStack.isNotEmpty())
			newContinuation = newContinuation.pushFrame(callStack.removeLast())

		return OperationResult(image.copy(stack = newStack).withUsedOp(), listOf(), newContinuation, HexEvalSounds.SPELL)
	}
}