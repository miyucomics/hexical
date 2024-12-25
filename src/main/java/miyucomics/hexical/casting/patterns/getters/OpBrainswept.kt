package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.mob.MobEntity

class OpBrainswept : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val entity = args.getEntity(0, argc)
		ctx.assertEntityInRange(entity)
		if (entity is MobEntity)
			return IXplatAbstractions.INSTANCE.isBrainswept(entity).asActionResult
		return (false).asActionResult
	}
}