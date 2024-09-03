package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.FrameForEach
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation.NotDone
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsThothMishap
import miyucomics.hexical.enums.InjectedGambit
import miyucomics.hexical.interfaces.FrameForEachMinterface

// prevents IDE from yelling at us for casting FrameForEach since we have a minterface
@Suppress("CAST_NEVER_SUCCEEDS")

object OpAtalanta : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		val newStack = stack.toList()
		var newCont = continuation

		while (true) {
			if (newCont !is NotDone)
				throw NeedsThothMishap()
			val frame = newCont.frame
			if (frame is FrameForEach) {
				val injectedGambit = (frame as FrameForEachMinterface).getInjectedGambit()
				if (injectedGambit == InjectedGambit.NONE || injectedGambit == InjectedGambit.SISYPHUS)
					break
			}
			newCont = newCont.next
		}

		return OperationResult(newCont, newStack, ravenmind, listOf())
	}
}