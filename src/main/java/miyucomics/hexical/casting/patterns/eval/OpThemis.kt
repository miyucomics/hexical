package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.casting.ResolvedPatternType
import at.petrak.hexcasting.api.spell.casting.eval.FrameEvaluate
import at.petrak.hexcasting.api.spell.casting.eval.FrameForEach
import at.petrak.hexcasting.api.spell.casting.eval.FunctionalData
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.casting.mishaps.ThemisMishap
import miyucomics.hexical.enums.InjectedGambit
import miyucomics.hexical.interfaces.FrameForEachMinterface

object OpThemis : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.size < 2)
			throw MishapNotEnoughArgs(1, 0)
		val data = stack.getList(stack.lastIndex - 1)
		val code = evaluatable(stack[stack.lastIndex], 0).map({ SpellList.LList(0, listOf(PatternIota(it))) }, { it })
		stack.removeLastOrNull()
		stack.removeLastOrNull()

		if (data.size() == 0) {
			stack.add(ListIota(listOf()))
			return OperationResult(continuation, stack, ravenmind, listOf())
		}

		val frame = FrameForEach(data, code, null, mutableListOf())
		(frame as FrameForEachMinterface).overwrite(InjectedGambit.THEMIS)
		return OperationResult(continuation.pushFrame(frame), stack, ravenmind, listOf())
	}

	@JvmStatic
	fun breakDownwards(baseStack: List<Iota>, accumulator: MutableList<Iota>): Pair<Boolean, List<Iota>> {
		val final = mutableListOf<Iota>()
		val itemPriorityPairs = accumulator.chunked(2)
		val sortedPairs = itemPriorityPairs.sortedBy { (it[1] as DoubleIota).double }
		sortedPairs.forEach { final.add(it[0]) }

		val newStack = baseStack.toMutableList()
		newStack.add(ListIota(final))
		return true to newStack
	}

	@JvmStatic
	fun evaluate(continuation: SpellContinuation, harness: CastingHarness, data: SpellList, code: SpellList, baseStack: List<Iota>?, accumulator: MutableList<Iota>): CastingHarness.CastResult {
		val stack: List<Iota> = if (baseStack == null) {
			harness.stack.toList()
		} else {
			val top = harness.stack.lastOrNull() ?: throw ThemisMishap()
			if (top !is DoubleIota)
				throw ThemisMishap()
			accumulator.add(top)
			baseStack
		}

		val (stackTop, newContinuation) = if (data.nonEmpty) {
			harness.ctx.incDepth()
			val frame = FrameForEach(data.cdr, code, stack, accumulator)
			(frame as FrameForEachMinterface).overwrite(InjectedGambit.THEMIS)
			accumulator.add(data.car)
			data.car to continuation.pushFrame(frame).pushFrame(FrameEvaluate(code, true))
		} else {
			val final = mutableListOf<Iota>()
			val itemPriorityPairs = accumulator.chunked(2)
			val sortedPairs = itemPriorityPairs.sortedBy { (it[1] as DoubleIota).double }
			sortedPairs.forEach { final.add(it[0]) }
			ListIota(final) to continuation
		}

		val newStack = stack.toMutableList()
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