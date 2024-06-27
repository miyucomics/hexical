package miyucomics.hexical.casting.patterns.soroban

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.interfaces.CastingContextMinterface

class OpSorobanIncrement : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		(ctx as CastingContextMinterface).incrementSoroban()
		return listOf(DoubleIota(ctx.getSoroban().toDouble() - 1))
	}
}