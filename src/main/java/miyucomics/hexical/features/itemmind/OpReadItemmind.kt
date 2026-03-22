package miyucomics.hexical.features.itemmind

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.features.charms.CharmCastEnv

object OpReadItemmind : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = when (env) {
			is CharmCastEnv -> env.stack
			is PackagedItemCastEnv -> env.caster!!.getStackInHand(env.castingHand)
			else -> throw NeedsItemBoundCastEnvMishap()
		}.nbt ?: throw NeedsItemBoundCastEnvMishap()

		if (stack.contains("itemmind"))
			return listOf(IotaType.deserialize(stack.getCompound("itemmind"), env.world))

		return listOf(NullIota())
	}
}