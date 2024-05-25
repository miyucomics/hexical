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
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.enums.InjectedGambit
import miyucomics.hexical.interfaces.FrameForEachMinterface

object OpSisyphus : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.size == 0)
			throw MishapNotEnoughArgs(1, 0)
		val code = stack.getList(stack.lastIndex)
		stack.removeLastOrNull()
		if (code.size() == 0)
			return OperationResult(continuation, stack, ravenmind, listOf())
		val frame = FrameForEach(SpellList.LList(0, listOf()), code, stack, mutableListOf())
		(frame as FrameForEachMinterface).overwrite(InjectedGambit.SISYPHUS)
		return OperationResult(continuation.pushFrame(frame), stack, ravenmind, listOf())
	}

	fun breakDownwards(baseStack: List<Iota>): Pair<Boolean, List<Iota>> = true to baseStack

	fun evaluate(continuation: SpellContinuation, harness: CastingHarness, code: SpellList, baseStack: List<Iota>): CastingHarness.CastResult {
		harness.ctx.incDepth()
		val frame = FrameForEach(SpellList.LList(0, listOf()), code, baseStack, mutableListOf())
		(frame as FrameForEachMinterface).overwrite(InjectedGambit.SISYPHUS)
		return CastingHarness.CastResult(
			continuation.pushFrame(frame).pushFrame(FrameEvaluate(code, true)),
			FunctionalData(baseStack, 0, listOf(), false, harness.ravenmind),
			ResolvedPatternType.EVALUATED,
			listOf(),
			HexEvalSounds.THOTH,
		)
	}
}