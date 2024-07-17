package miyucomics.hexical.casting.patterns.pigment

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getDouble
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d

class OpQueryPigment : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val location = args.getVec3(0, argc)
		val time = args.getDouble(1, argc)
		val color = IXplatAbstractions.INSTANCE.getColorizer(ctx.caster).getColor(time.toFloat(), location)
		return Vec3d(ColorHelper.Argb.getRed(color).toDouble(), ColorHelper.Argb.getGreen(color).toDouble(), ColorHelper.Argb.getBlue(color).toDouble()).multiply(1.0 / 255.0).asActionResult
	}
}