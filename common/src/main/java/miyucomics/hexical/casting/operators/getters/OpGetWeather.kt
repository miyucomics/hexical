package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota

class OpGetWeather : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (ctx.caster.world.isThundering)
			return listOf(DoubleIota(2.0))
		if (ctx.caster.world.isRaining)
			return listOf(DoubleIota(1.0))
		return listOf(DoubleIota(0.0))
	}
}