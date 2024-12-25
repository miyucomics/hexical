package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.entity.LivingEntity

class OpGetLivingEntityData(private val process: (LivingEntity) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val iota = args[0]
		if (iota !is EntityIota)
			throw MishapInvalidIota.ofType(iota, 0, "lenient_living")
		val entity = iota.entity
		env.assertEntityInRange(entity)
		if (entity !is LivingEntity)
			throw MishapInvalidIota.ofType(iota, 0, "lenient_living")
		return process(entity)
	}
}