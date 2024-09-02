package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.ContinuationFrame
import at.petrak.hexcasting.api.spell.casting.eval.FrameForEach
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation.NotDone
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsThothMishap

/*
for example:

val continuation = NotDone(
	FrameEvaluate(), [0]
	NotDone(
		FrameFinishEval(), [1]
		NotDone(
			FrameForEach(), [2]
			NotDone( [3]
				FrameEvaluate(),
				Done()
			)
		)
	)
)

val callStack = mutableListOf(
	FrameEvaluate(), [0]
	FrameFinishEval(), [1]
)
val forEach = FrameForEach() [2]
var newCont = NotDone(...) [3]
*/

/** Modifies the highest FrameForEach in the call stack. */
abstract class OpModifyThoth : Action {
	/** Given the current FrameForEach being evaluated, returns a new FrameForEach to replace it with. */
	abstract fun updateFrame(frame: FrameForEach, stack: MutableList<Iota>): FrameForEach

	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		// pop frames off of continuation until we find a FrameForEach or run out of frames
		// after this loop, callStack is everything above forEach, and newCont is everything below it
		val callStack = mutableListOf<ContinuationFrame>()
		val forEach: FrameForEach
		var newCont = continuation
		while (true) {
			if (newCont !is NotDone) {
				// if we reach a Done continuation without finding a FrameForEach, then we're not in a thoth loop
				throw NeedsThothMishap()
			}
			val frame = newCont.frame
			newCont = newCont.next
			when (frame) {
				is FrameForEach -> {
					forEach = frame
					break
				}
				else -> callStack.add(frame)
			}
		}

		// push a modified copy of forEach back onto the frame, with the iota added to data
		newCont = newCont.pushFrame(updateFrame(forEach, stack))

		// re-push the frames in the opposite order we popped them so that it ends up back in the original order
		while (callStack.isNotEmpty()) {
			newCont = newCont.pushFrame(callStack.removeLast())
		}

		return OperationResult(newCont, stack, ravenmind, listOf())
	}
}
