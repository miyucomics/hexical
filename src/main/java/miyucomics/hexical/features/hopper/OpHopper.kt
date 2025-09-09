package miyucomics.hexical.features.hopper

import at.petrak.hexcasting.api.HexAPI
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
import net.minecraft.item.ItemStack

object OpHopper : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		var inputsConsumed = 0

		var inputSlot: Int? = null
		var outputSlot: Int? = null
		if (stack.isNotEmpty() && stack.last() is DoubleIota) {
			outputSlot = (stack.removeAt(stack.lastIndex) as DoubleIota).double.toInt()
			inputsConsumed += 1
		}

		if (stack.isEmpty())
			throw MishapNotEnoughArgs(2, 1)
		val destinationIota = stack.removeAt(stack.lastIndex)
		val destination = HopperEndpointRegistry.resolve(destinationIota, env, outputSlot) as? HopperDestination
			?: throw MishapInvalidIota.of(destinationIota, inputsConsumed, "hopper_destination")
		inputsConsumed += 1

		if (stack.isNotEmpty() && stack.last() is DoubleIota) {
			inputSlot = (stack.removeAt(stack.lastIndex) as DoubleIota).double.toInt()
			inputsConsumed += 1
		}

		if (stack.isEmpty())
			throw MishapNotEnoughArgs(inputsConsumed + 1, inputsConsumed)
		val sourceIota = stack.removeAt(stack.lastIndex)
		val source = HopperEndpointRegistry.resolve(sourceIota, env, inputSlot) as? HopperSource
			?: throw MishapInvalidIota.of(sourceIota, inputsConsumed, "hopper_source")

		val itemsToMove = destination.simulateDeposits(source.getItems())
		val totalItems = itemsToMove.values.sum()

		return OperationResult(
			image.withUsedOp().copy(stack = stack), listOf(
				OperatorSideEffect.ConsumeMedia(totalItems * MediaConstants.DUST_UNIT * 3 / 64),
				OperatorSideEffect.AttemptSpell(Spell(source, destination, itemsToMove), hasCastingSound = true, awardStat = true)
			), continuation, HexEvalSounds.SPELL
		)
	}

	private data class Spell(val source: HopperSource, val destination: HopperDestination, val itemsToMove: Map<ItemStack, Int>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			for (entry in itemsToMove) {
				val didWithdraw = source.withdraw(entry.key, entry.value)
				if (!didWithdraw)
					continue
				// deposit() returns the remainder stack, so we can subtract its count from 
				// the initial count to find how many items were actually inserted
				val added = entry.key.count - destination.deposit(entry.key.copy()).count
				if (added > entry.value)
					HexAPI.LOGGER.warn("OpHopper somehow added more items ($added) than expected (${entry.value})")
				if (added < entry.value)
					HexAPI.LOGGER.warn("OpHopper somehow added fewer items ($added) than expected (${entry.value})")
			}
		}
	}
}