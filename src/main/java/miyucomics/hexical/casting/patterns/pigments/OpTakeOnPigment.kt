package miyucomics.hexical.casting.patterns.pigments

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.iota.getPigment

class OpTakeOnPigment : ConstMediaAction {
	override val argc = 1
	override val mediaCost = MediaConstants.DUST_UNIT / 8
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val pigment = args.getPigment(0, 1)
		IXplatAbstractions.INSTANCE.setColorizer(ctx.caster, pigment)
		return listOf()
	}
}