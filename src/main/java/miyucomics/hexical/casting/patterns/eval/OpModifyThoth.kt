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
import miyucomics.hexical.enums.InjectedGambit
import miyucomics.hexical.interfaces.FrameForEachMinterface

@Suppress("CAST_NEVER_SUCCEEDS", "KotlinRedundantDiagnosticSuppress")
abstract class OpModifyThoth : Action {
	abstract fun updateFrame(frame: FrameForEach, stack: MutableList<Iota>): FrameForEach

	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		val callStack = mutableListOf<ContinuationFrame>()
		val forEach: FrameForEach
		var newCont = continuation

		while (true) {
			if (newCont !is NotDone)
				throw NeedsThothMishap()
			val frame = newCont.frame
			newCont = newCont.next

			if (frame is FrameForEach && (frame as FrameForEachMinterface).getInjectedGambit() == InjectedGambit.NONE) {
				forEach = frame
				break
			} else {
				callStack.add(frame)
			}
		}

		newCont = newCont.pushFrame(updateFrame(forEach, stack))
		while (callStack.isNotEmpty())
			newCont = newCont.pushFrame(callStack.removeLast())

		return OperationResult(newCont, stack, ravenmind, listOf())
	}
}
