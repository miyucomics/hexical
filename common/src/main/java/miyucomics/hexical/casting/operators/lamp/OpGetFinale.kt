package miyucomics.hexical.casting.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface

class OpGetFinale : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val minterface = ctx as CastingContextMinterface
		if (minterface.getSpecializedSource() == SpecializedSource.HAND_LAMP && minterface.getSpecializedSource() == SpecializedSource.ARCH_LAMP)
			return listOf(BooleanIota(minterface.getFinale()))
		return listOf(NullIota())
	}
}