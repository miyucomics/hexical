package miyucomics.hexical.casting.operators.eval

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.FrameEvaluate
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

object OpNephthys : Action {
	override fun operate(continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext): OperationResult {
		if (stack.size < 2)
			throw MishapNotEnoughArgs(2, stack.size)

		val rawInstructions = stack[stack.lastIndex - 1]
		val instructions = evaluatable(rawInstructions, 1).map({ SpellList.LList(0, listOf(PatternIota(it))) }, { it })
		val depth = stack.getPositiveInt(stack.lastIndex)
		stack.removeLast()
		stack.removeLast()

		val restoreIota = SpellList.LList(0, listOf(
			PatternIota(HexPattern.fromAngles("qqqaw", HexDir.WEST)),
			ListIota(stack.popStack(depth)),
			PatternIota(HexPattern.fromAngles("qwaeawq", HexDir.WEST))
		))

		return OperationResult(
			continuation
				.pushFrame(FrameEvaluate(restoreIota, true))
				.pushFrame(FrameEvaluate(instructions, true)),
			stack, ravenmind, listOf()
		)
	}

	private fun MutableList<Iota>.popStack(argc: Int): List<Iota> {
		val stackSize = this.size
		if (stackSize < argc)
			throw MishapNotEnoughArgs(argc, stackSize)
		val args = this.subList(stackSize - argc, stackSize).toList()
		repeat(argc) { this.removeLast() }
		return args
	}
}