package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.data.hopper.HopperEndpointRegistry
import miyucomics.hexical.data.hopper.HopperEndpoint

class OpHopper : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val source = HopperEndpointRegistry.resolve(args[0], env) as? HopperEndpoint.Source ?: throw MishapInvalidIota.of(args[0], 2, "hopper_source_endpoint")
		val destination = HopperEndpointRegistry.resolve(args[1], env) ?: throw MishapInvalidIota.of(args[1], 1, "hopper_endpoint")
//		val hex = args.getList(2, argc)

		source.getItems().forEach { stack ->
			destination.insert(stack)
		}

		return SpellAction.Result(Spell(source, destination), MediaConstants.SHARD_UNIT, listOf())
	}

	private data class Spell(val from: HopperEndpoint, val to: HopperEndpoint) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {

		}
	}
}