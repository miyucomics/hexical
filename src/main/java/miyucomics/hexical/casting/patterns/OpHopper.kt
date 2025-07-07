package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.data.hopper.HopperDestination
import miyucomics.hexical.data.hopper.HopperEndpointRegistry
import miyucomics.hexical.data.hopper.HopperSource

class OpHopper : ConstMediaAction {
	override val argc = 2
	override val mediaCost = MediaConstants.SHARD_UNIT
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val source = HopperEndpointRegistry.resolve(args[0], env) as? HopperSource ?: throw MishapInvalidIota.of(args[0], 2, "hopper_source")
		val destination = HopperEndpointRegistry.resolve(args[1], env) as? HopperDestination ?: throw MishapInvalidIota.of(args[1], 1, "hopper_destination")

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

		return listOf()
	}
}