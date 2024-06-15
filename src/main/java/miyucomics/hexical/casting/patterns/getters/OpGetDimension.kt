package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.iota.IdentifierIota

class OpGetDimension : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		return listOf(IdentifierIota(ctx.world.registryKey.value))
	}
}