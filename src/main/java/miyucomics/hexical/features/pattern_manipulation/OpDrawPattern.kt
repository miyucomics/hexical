package miyucomics.hexical.features.pattern_manipulation

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.misc.RenderUtils
import net.minecraft.util.math.Vec3d

object OpDrawPattern : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val output = mutableListOf<Vec3Iota>()
		RenderUtils.getNormalizedStrokes(args.getPattern(0, argc)).forEach { vec -> output.add(Vec3Iota(Vec3d(vec.x.toDouble(), vec.y.toDouble(), 0.0))) }
		return output.toList().asActionResult
	}
}