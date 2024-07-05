package miyucomics.hexical.casting.patterns.basic

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getDouble
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.HexicalMain

class OpPerlin : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val location = args.getVec3(0, argc).multiply(0.25) // reduce it so the change isn't so dramatic
		return (HexicalMain.PERLIN_NOISE.evaluateNoise(location.x, location.y, location.z, args.getDouble(1, argc) * 0.25, ctx.world.seed) / 2 + 0.5).asActionResult
	}
}