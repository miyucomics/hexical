package miyucomics.hexical.casting.operators.basic

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPattern
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota

class OpCongruentPattern : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val a = args.getPattern(0, argc)
		val b = args.getPattern(1, argc)
		return listOf(BooleanIota(a.anglesSignature() == b.anglesSignature()))
	}
}