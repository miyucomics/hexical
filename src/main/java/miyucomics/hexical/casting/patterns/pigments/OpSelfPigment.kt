package miyucomics.hexical.casting.patterns.pigments

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.iota.PigmentIota

class OpSelfPigment : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext) = listOf(PigmentIota(IXplatAbstractions.INSTANCE.getColorizer(ctx.caster)))
}