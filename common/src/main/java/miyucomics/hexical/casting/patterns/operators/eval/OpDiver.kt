package miyucomics.hexical.casting.patterns.operators.eval

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

object OpDiver : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.size < 2)
			throw MishapNotEnoughArgs(2, stack.size)
		val instructions = stack.getList(stack.lastIndex - 1)
		val diveAmount = stack.getPositiveIntUnderInclusive(0, stack.size - 1)
		stack.removeLastOrNull()
		stack.removeLastOrNull()

		val output = mutableListOf<Iota>()
		output.addAll(stack.takeLast(diveAmount))
		for (i in 0 until diveAmount)
			stack.removeLast()

		// cast pattern list??

		output.reverse()
		for (iota in output)
			stack.add(iota)

		return OperationResult(continuation, stack, ravenmind, listOf())
	}
}