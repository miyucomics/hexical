package miyucomics.hexical.casting.operators.eval

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.SpellList
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.casting.ResolvedPatternType
import at.petrak.hexcasting.api.spell.casting.eval.FrameEvaluate
import at.petrak.hexcasting.api.spell.casting.eval.FrameForEach
import at.petrak.hexcasting.api.spell.casting.eval.FunctionalData
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.enums.InjectedGambit
import miyucomics.hexical.interfaces.FrameForEachMinterface

object OpThemis : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.size < 2)
			throw MishapNotEnoughArgs(1, 0)
		val data = stack.getList(stack.lastIndex - 1)
		val code = stack.getList(stack.lastIndex)
		stack.removeLastOrNull()
		stack.removeLastOrNull()
		if (data.size() == 0)
			return OperationResult(continuation, stack, ravenmind, listOf())
		if (code.size() == 0)
			return OperationResult(continuation, stack, ravenmind, listOf())
		val frame = FrameForEach(data, code, null, mutableListOf())
		(frame as FrameForEachMinterface).overwrite(InjectedGambit.THEMIS)
		return OperationResult(continuation.pushFrame(frame), stack, ravenmind, listOf())
	}

	fun breakDownwards(stack: List<Iota>, accumulator: MutableList<Iota>): Pair<Boolean, List<Iota>> {
		val newStack = stack.toMutableList()
		return true to newStack
	}

	fun evaluate(continuation: SpellContinuation, harness: CastingHarness, data: SpellList, code: SpellList, baseStack: List<Iota>?, accumulator: MutableList<Iota>): CastingHarness.CastResult {
		val (stackTop, newContinuation) = if (data.nonEmpty) {
			harness.ctx.incDepth()
			val frame = FrameForEach(data.cdr, code, baseStack, accumulator)
			(frame as FrameForEachMinterface).overwrite(InjectedGambit.THEMIS)
			data.car to continuation.pushFrame(frame).pushFrame(FrameEvaluate(code, true))
		} else {
			ListIota(accumulator) to continuation
		}

		val newStack = baseStack.toMutableList()
		newStack.add(stackTop)

		return CastingHarness.CastResult(
			newContinuation,
			FunctionalData(newStack, 0, listOf(), false, harness.ravenmind),
			ResolvedPatternType.EVALUATED,
			listOf(),
			HexEvalSounds.THOTH,
		)
	}
}