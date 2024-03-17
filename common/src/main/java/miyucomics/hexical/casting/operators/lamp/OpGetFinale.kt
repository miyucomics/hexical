package miyucomics.hexical.casting.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsLampMishap
import miyucomics.hexical.interfaces.CastingContextMixinInterface
import miyucomics.hexical.registry.HexicalItems

class OpGetFinale : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!(ctx as CastingContextMixinInterface).getCastByLamp())
			throw NeedsLampMishap()
		return listOf(BooleanIota((ctx as CastingContextMixinInterface).getFinale()))
	}
}