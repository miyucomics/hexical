package miyucomics.hexical.casting.patterns.colors

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.iota.getTrueDye
import net.minecraft.util.math.Vec3d

class OpTranslateDye : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val dye = args.getTrueDye(0, argc).colorComponents
		return Vec3d(dye[0].toDouble(), dye[1].toDouble(), dye[2].toDouble()).asActionResult
	}
}