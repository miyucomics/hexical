package miyucomics.hexical.casting.patterns.staff

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap
import miyucomics.hexical.utils.CastingUtils

class OpWriteStaff : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if ((env as CastingEnvironmentMinterface).getSpecializedSource() != SpecializedSource.CONJURED_STAFF)
			throw NeedsSourceMishap("conjured_staff")
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env.caster)
		env.caster.getStackInHand(env.castingHand).orCreateNbt.putCompound("storage", HexIotaTypes.serialize(iota))
		return listOf()
	}
}