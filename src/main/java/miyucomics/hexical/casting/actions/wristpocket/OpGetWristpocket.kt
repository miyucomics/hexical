package miyucomics.hexical.casting.actions.wristpocket

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.casting.mishaps.NeedsWristpocketMishap
import miyucomics.hexical.features.wristpocket.WristpocketUtils
import miyucomics.hexpose.iotas.asActionResult

class OpGetWristpocket : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val wristpocket = WristpocketUtils.getWristpocketStack(env) ?: throw NeedsWristpocketMishap()
		if (wristpocket.isEmpty)
			return listOf(NullIota())
		return wristpocket.asActionResult
	}
}