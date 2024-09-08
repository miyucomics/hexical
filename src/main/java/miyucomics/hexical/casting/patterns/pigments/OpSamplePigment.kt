package miyucomics.hexical.casting.patterns.pigments

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getDouble
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.iota.getPigment
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d

class OpSamplePigment : ConstMediaAction {
	override val argc = 3
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val pigment = args.getPigment(0, argc)
		val location = args.getVec3(1, argc)
		val time = args.getDouble(2, argc)
		val color = pigment.getColor(time.toFloat(), location)
		return Vec3d(ColorHelper.Argb.getRed(color).toDouble() / 255.0, ColorHelper.Argb.getGreen(color).toDouble() / 255.0, ColorHelper.Argb.getBlue(color).toDouble() / 255.0).asActionResult
	}
}