package miyucomics.hexical.casting.patterns.prestidigitation

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.data.PrestidigitationData

class OpCanPrestidigitation : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				(PrestidigitationData.entityEffect(entity) != null).asActionResult
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				(PrestidigitationData.blockEffect(env.world.getBlockState(position).block) != null).asActionResult
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}
	}
}