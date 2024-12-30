package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota

class OpGetAmbit : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return when (val iota = args[0]) {
			is EntityIota -> env.isEntityInRange(iota.entity)
			is Vec3Iota -> env.isVecInRange(iota.vec3)
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}.asActionResult
	}
}