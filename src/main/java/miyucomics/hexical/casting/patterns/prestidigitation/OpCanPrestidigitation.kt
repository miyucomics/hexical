package miyucomics.hexical.casting.patterns.prestidigitation

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.data.PrestidigitationData

class OpCanPrestidigitation : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		return listOf(when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				ctx.assertEntityInRange(entity)
				BooleanIota(PrestidigitationData.entityEffect(entity) != null)
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				ctx.assertVecInRange(position)
				BooleanIota(PrestidigitationData.blockEffect(ctx.world.getBlockState(position).block) != null)
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		})
	}
}