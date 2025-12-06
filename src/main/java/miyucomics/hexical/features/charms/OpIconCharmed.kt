package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.util.math.ColorHelper
import kotlin.math.max
import kotlin.math.min

object OpIconCharmed : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is CharmCastEnv)
			throw NeedsCharmedItemMishap()
		when (val iota = args[0]) {
			is NullIota -> CharmUtilities.getCompound(env.stack).remove("icon")
			is Vec3Iota -> CharmUtilities.getCompound(env.stack).putInt("icon", ColorHelper.Argb.getArgb(
				255,
				(max(min(iota.vec3.x, 1.0), 0.0) * 255).toInt(),
				(max(min(iota.vec3.y, 1.0), 0.0) * 255).toInt(),
				(max(min(iota.vec3.z, 1.0), 0.0) * 255).toInt()
			))
			else -> throw MishapInvalidIota.of(iota, 0, "vector_or_null")
		}
		return emptyList()
	}
}