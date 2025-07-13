package miyucomics.hexical.casting.actions.pigments

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.iotas.getPigment
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d

class OpSamplePigment : ConstMediaAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val pigment = args.getPigment(0, argc)
		val location = args.getVec3(1, argc)
		val time = args.getDouble(2, argc)
		val color = pigment.colorProvider.getColor(time.toFloat(), location)
		return Vec3d(ColorHelper.Argb.getRed(color).toDouble() / 255.0, ColorHelper.Argb.getGreen(color).toDouble() / 255.0, ColorHelper.Argb.getBlue(color).toDouble() / 255.0).asActionResult
	}
}