package miyucomics.hexical.casting.patterns.pigments

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.iotas.getPigment

class OpTakeOnPigment : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		env.pigment = args.getPigment(0, 1)
		return listOf()
	}
}