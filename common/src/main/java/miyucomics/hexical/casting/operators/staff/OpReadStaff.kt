package miyucomics.hexical.casting.operators.staff

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface

class OpReadStaff : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if ((ctx as CastingContextMinterface).getSpecializedSource() != SpecializedSource.CONJURED_STAFF)
			throw NeedsSourceMishap("conjured_staff")
		val nbt = ctx.caster.getStackInHand(ctx.castingHand).orCreateNbt
		if (nbt.contains("storage"))
			return listOf(HexIotaTypes.deserialize(nbt.getCompound("storage"), ctx.world))
		return listOf(NullIota())
	}
}