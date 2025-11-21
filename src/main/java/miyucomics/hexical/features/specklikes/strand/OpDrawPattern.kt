package miyucomics.hexical.features.specklikes.strand

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.misc.PatternUtils
import net.minecraft.util.math.Vec3d

object OpDrawPattern : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> = PatternUtils.getNormalizedStrokes(args.getPattern(0, argc)).map { Vec3Iota(Vec3d(it.x.toDouble(), it.y.toDouble(), 0.0)) }.asActionResult
}