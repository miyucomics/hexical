package miyucomics.hexical.casting.operators

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.persistent_state.TelepathyData

class OpGetTelepathy : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val caster = ctx.caster.uuid
		if (!TelepathyData.active.contains(caster))
			return listOf(NullIota())
		if (!TelepathyData.active[caster]!!)
			return listOf(NullIota())
		return listOf(DoubleIota(TelepathyData.timer[caster]!!.toDouble()))
	}
}