package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import net.minecraft.entity.LivingEntity

class OpGetLivingEntityData(private val process: (LivingEntity) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val iota = args[0]
		if (iota !is EntityIota)
			throw MishapInvalidIota.ofType(iota, 0, "lenient_living")
		val entity = iota.entity
		if (entity !is LivingEntity)
			throw MishapInvalidIota.ofType(iota, 0, "lenient_living")
		return process(entity)
	}
}