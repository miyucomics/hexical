@file:Suppress("KotlinRedundantDiagnosticSuppress")

package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.FrameForEach
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.casting.frames.SisyphusFrame
import miyucomics.hexical.casting.frames.ThemisFrame
import miyucomics.hexical.casting.mishaps.NeedsSkippableMishap

object OpAtalanta : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		var newContinuation = continuation

		while (true) {
			if (newContinuation !is SpellContinuation.NotDone)
				throw NeedsSkippableMishap()
			val frame = newContinuation.frame
			when (frame) {
				is FrameForEach -> break
				is SisyphusFrame -> break
				is ThemisFrame -> break
				else -> newContinuation = newContinuation.next
			}
		}

		return OperationResult(image.withUsedOp(), listOf(), newContinuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}