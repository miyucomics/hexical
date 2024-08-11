package miyucomics.hexical.casting.patterns.colors

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.iota.getTrueDye
import net.minecraft.util.math.Vec3d

class OpTranslateDye : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val dye = args.getTrueDye(0, argc).colorComponents
		return Vec3d(dye[0].toDouble(), dye[1].toDouble(), dye[2].toDouble()).asActionResult
	}
}