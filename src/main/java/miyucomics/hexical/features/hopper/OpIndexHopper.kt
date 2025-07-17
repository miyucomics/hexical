package miyucomics.hexical.features.hopper

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.features.hopper.targets.InventoryEndpoint
import miyucomics.hexpose.iotas.ItemStackIota

object OpIndexHopper : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val source = HopperEndpointRegistry.resolve(args[0], env, null) as? HopperSource ?: throw MishapInvalidIota.of(args[0], 0, "inventory")
		if (source !is InventoryEndpoint)
			throw MishapInvalidIota.of(args[0], 0, "inventory")
		val inventory = source.inventory
		return (0 until inventory.size()).map { ItemStackIota.createOptimized(inventory.getStack(it)) }.asActionResult
	}
}