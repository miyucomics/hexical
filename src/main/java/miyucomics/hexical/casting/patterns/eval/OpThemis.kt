package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.evaluatable
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.casting.frames.ThemisFrame

object OpThemis : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.size < 2)
			throw MishapNotEnoughArgs(1, 0)
		val data = stack.getList(stack.lastIndex - 1)
		val code = evaluatable(stack[stack.lastIndex], 0).map({ SpellList.LList(0, listOf(it)) }, { it })
		stack.removeLastOrNull()
		stack.removeLastOrNull()

		if (data.size() == 0) {
			stack.add(ListIota(listOf()))
			return OperationResult(image.withResetEscape().copy(stack = stack), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
		}

		return OperationResult(
			image.withResetEscape().copy(stack = stack),
			listOf(),
			continuation.pushFrame(ThemisFrame(data, code, null, mutableListOf(), mutableListOf())),
			HexEvalSounds.THOTH
		)
	}
}