package miyucomics.hexical.features.itemmind

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.features.charms.CharmCastEnv
import miyucomics.hexical.misc.CastingUtils

object OpWriteItemmind : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = when (env) {
			is CharmCastEnv -> env.stack
			is PackagedItemCastEnv -> env.caster!!.getStackInHand(env.castingHand)
			else -> throw NeedsItemBoundCastEnvMishap()
		}.nbt ?: throw NeedsItemBoundCastEnvMishap()

		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env)
		stack.putCompound("itemmind", IotaType.serialize(iota))
		return emptyList()
	}
}