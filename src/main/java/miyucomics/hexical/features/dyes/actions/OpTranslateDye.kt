package miyucomics.hexical.features.dyes.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.features.dyes.getColoredDye
import net.minecraft.util.math.Vec3d

object OpTranslateDye : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val dye = args.getColoredDye(0, argc).colorComponents
		return Vec3d(dye[0].toDouble(), dye[1].toDouble(), dye[2].toDouble()).asActionResult
	}
}