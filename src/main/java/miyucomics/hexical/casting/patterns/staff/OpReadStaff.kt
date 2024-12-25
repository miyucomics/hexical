package miyucomics.hexical.casting.patterns.staff

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap

class OpReadStaff : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if ((env as CastingEnvironmentMinterface).getSpecializedSource() != SpecializedSource.CONJURED_STAFF)
			throw NeedsSourceMishap("conjured_staff")
		val nbt = env.caster.getStackInHand(env.castingHand).orCreateNbt
		if (nbt.contains("storage"))
			return listOf(HexIotaTypes.deserialize(nbt.getCompound("storage"), env.world))
		return listOf(NullIota())
	}
}