package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.mishaps.NeedsWristpocketMishap
import miyucomics.hexical.utils.WristpocketUtils
import net.minecraft.item.ItemStack

class OpGetWristpocketData(private val process: (ItemStack) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val wristpocket = WristpocketUtils.getWristpocketStack(env) ?: throw NeedsWristpocketMishap()
		return process(wristpocket)
	}
}