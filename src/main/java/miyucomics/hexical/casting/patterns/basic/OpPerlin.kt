package miyucomics.hexical.casting.patterns.basic

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getDouble
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.utils.PerlinNoise

class OpPerlin : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val location = args.getVec3(0, argc).multiply(0.25)
		return (PerlinNoise(ctx.world.seed.toInt()).noise(location.x, location.y, location.z, args.getDouble(1, argc) * 0.25) / 2 + 0.5).asActionResult
	}
}