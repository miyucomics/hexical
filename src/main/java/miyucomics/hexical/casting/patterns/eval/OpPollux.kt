package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.SpellList
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.FrameFinishEval
import at.petrak.hexcasting.api.spell.casting.eval.FrameForEach
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

object OpPollux : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.size < 1)
			throw MishapNotEnoughArgs(1, 0)
		val iota = stack[stack.lastIndex]
		stack.removeLastOrNull()

		if (continuation is SpellContinuation.NotDone && continuation.frame is FrameFinishEval) {
			val next = continuation.next
			if (next is SpellContinuation.NotDone && next.frame is FrameForEach) {
				val thoth = (next.frame as FrameForEach)
				val newIterator = thoth.data.cdr.toMutableList()
				newIterator.add(iota)
				return OperationResult(continuation.pushFrame(FrameForEach(SpellList.LList(newIterator), thoth.code, thoth.baseStack, thoth.acc)), stack, ravenmind, listOf())
			}
		}

		return OperationResult(continuation, stack, ravenmind, listOf())
	}
}