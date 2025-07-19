package miyucomics.hexical.features.wristpocket

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.asActionResult

object OpGetWristpocket : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val wristpocket = WristpocketUtils.getWristpocketStack(env) ?: throw NeedsWristpocketMishap()
		if (wristpocket.isEmpty)
			return listOf(NullIota())
		return wristpocket.asActionResult
	}
}