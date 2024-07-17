package miyucomics.hexical.casting.patterns.staff

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface
import miyucomics.hexical.utils.CastingUtils

class OpWriteStaff : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if ((ctx as CastingContextMinterface).getSpecializedSource() != SpecializedSource.CONJURED_STAFF)
			throw NeedsSourceMishap("conjured_staff")
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, ctx.caster)
		ctx.caster.getStackInHand(ctx.castingHand).orCreateNbt.putCompound("storage", HexIotaTypes.serialize(iota))
		return listOf()
	}
}