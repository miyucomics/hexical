package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.FrameFinishEval
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.evaluatable
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.casting.frames.NephthysFrame

class OpNephthys(private val depth: Int) : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.size < depth + 1)
			throw MishapNotEnoughArgs(depth + 1, stack.size)

		val instructions = evaluatable(stack[stack.lastIndex], 0).map({ SpellList.LList(0, listOf(it)) }, { it })
		stack.removeLast()

		return OperationResult(image, listOf(),
			continuation
				.pushFrame(FrameFinishEval)
				.pushFrame(NephthysFrame(stack.popStack(depth)))
				.pushFrame(FrameEvaluate(instructions, true)),
			HexEvalSounds.HERMES
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