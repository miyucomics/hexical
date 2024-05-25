package miyucomics.hexical.casting.operators.basic

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

class OpDupMany : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)
		val count = stack.takeLast(1).getPositiveIntUnderInclusive(0, stack.size - 1)
		stack.removeLast()
		for (iota in stack.takeLast(count))
			stack.add(iota)
		return OperationResult(continuation, stack, ravenmind, listOf())
	}
}