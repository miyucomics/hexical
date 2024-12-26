package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.utils.PerlinNoise

class OpPerlin : ConstMediaAction {
	private val scaling = 0.25
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val location = args.getVec3(0, argc).multiply(scaling)
		return (PerlinNoise(env.world.seed.toInt()).noise(location.x, location.y, location.z, args.getDouble(1, argc) * scaling) / 2 + 0.5).asActionResult
	}
}