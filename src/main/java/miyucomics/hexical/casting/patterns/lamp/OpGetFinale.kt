package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingEnvironmentMinterface

class OpGetFinale : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val minterface = ctx as CastingEnvironmentMinterface
		if (minterface.getSpecializedSource() == SpecializedSource.HAND_LAMP || minterface.getSpecializedSource() == SpecializedSource.ARCH_LAMP)
			return minterface.getFinale().asActionResult
		return listOf(NullIota())
	}
}