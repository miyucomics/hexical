package miyucomics.hexical.casting.patterns.colors

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
		return Vec3d(ColorHelper.Argb.getRed(color).toDouble() / 255.0, ColorHelper.Argb.getGreen(color).toDouble() / 255.0, ColorHelper.Argb.getBlue(color).toDouble() / 255.0).asActionResult
	}
}