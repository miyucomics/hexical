package miyucomics.hexical.casting.patterns.hopper

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.data.hopper.HopperDestination
import miyucomics.hexical.data.hopper.HopperEndpointRegistry
import miyucomics.hexical.data.hopper.HopperSource

object OpHopper : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		var inputsConsumed = 0

		var inputSlot: Int? = null
		var outputSlot: Int? = null
		if (stack.isNotEmpty() && stack.last() is DoubleIota) {
			outputSlot = (stack.removeLast() as DoubleIota).double.toInt()
			inputsConsumed += 1
		}

		if (stack.isEmpty())
			throw MishapNotEnoughArgs(2, 1)
		val destinationIota = stack.removeLast()
		val destination = HopperEndpointRegistry.resolve(destinationIota, env, outputSlot) as? HopperDestination
			?: throw MishapInvalidIota.Companion.of(destinationIota, inputsConsumed, "hopper_destination")
		inputsConsumed += 1

		if (stack.isNotEmpty() && stack.last() is DoubleIota) {
			inputSlot = (stack.removeLast() as DoubleIota).double.toInt()
			inputsConsumed += 1
		}

		if (stack.isEmpty())
			throw MishapNotEnoughArgs(inputsConsumed + 1, inputsConsumed)
		val sourceIota = stack.removeLast()
		val source = HopperEndpointRegistry.resolve(sourceIota, env, inputSlot) as? HopperSource
			?: throw MishapInvalidIota.Companion.of(sourceIota, inputsConsumed, "hopper_source")

		return OperationResult(
			image.withUsedOp().copy(stack = stack), listOf(
				OperatorSideEffect.ConsumeMedia(MediaConstants.SHARD_UNIT),
				OperatorSideEffect.AttemptSpell(
					Spell(source, destination),
					hasCastingSound = true,
					awardStat = true
				)
			), continuation, HexEvalSounds.SPELL
		)
	}

	private data class Spell(val source: HopperSource, val destination: HopperDestination) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			for (item in source.getItems()) {
				if (item.isEmpty)
					continue
				val simCount = destination.simulateDeposit(item)
				if (simCount <= 0)
					continue
				val toMove = item.copy()
				toMove.count = simCount
				val didWithdraw = source.withdraw(item, simCount)
				if (!didWithdraw)
					continue
				destination.deposit(toMove)
			}
		}
	}
}