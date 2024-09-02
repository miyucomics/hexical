package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

class OpSekhmet(private val perserve: Int) : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.size < perserve)
			throw MishapNotEnoughArgs(perserve, stack.size)
		return OperationResult(continuation, stack.takeLast(perserve), ravenmind, listOf())
	}
}