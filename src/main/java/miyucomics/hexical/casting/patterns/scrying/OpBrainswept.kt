package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.mob.MobEntity

class OpBrainswept : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getEntity(0, argc)
		env.assertEntityInRange(entity)
		if (entity is MobEntity)
			return IXplatAbstractions.INSTANCE.isBrainswept(entity).asActionResult
		return (false).asActionResult
	}
}