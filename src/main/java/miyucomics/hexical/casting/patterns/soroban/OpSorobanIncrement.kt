package miyucomics.hexical.casting.patterns.soroban

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.interfaces.CastingEnvironmentMinterface

class OpSorobanIncrement : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		(ctx as CastingEnvironmentMinterface).incrementSoroban()
		return (ctx.getSoroban().toDouble() - 1).asActionResult
	}
}