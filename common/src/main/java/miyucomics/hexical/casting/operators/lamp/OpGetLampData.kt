package miyucomics.hexical.casting.operators.lamp

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.utils.vecFromNBT
import miyucomics.hexical.casting.mishaps.NeedsLampMishap
import miyucomics.hexical.interfaces.CastingContextMinterface
import miyucomics.hexical.items.LampItem

class OpGetLampData(private val mode: Int) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!(ctx as CastingContextMinterface).getCastByLamp() || (ctx as CastingContextMinterface).getArchLamp())
			throw NeedsLampMishap()
		val nbt = ctx.caster.activeItem.nbt ?: return listOf(NullIota())
		return listOf(
			when (mode) {
				0 -> Vec3Iota(vecFromNBT(nbt.getLongArray("position")))
				1 -> Vec3Iota(vecFromNBT(nbt.getLongArray("rotation")))
				2 -> Vec3Iota(vecFromNBT(nbt.getLongArray("velocity")))
				3 -> DoubleIota(ctx.world.time - (nbt.getDouble("start_time") + 1.0))
				4 -> DoubleIota((ctx.caster.activeItem.item as LampItem).getMedia(ctx.caster.activeItem).toDouble() / MediaConstants.DUST_UNIT)
				else -> NullIota()
			}
		)
	}
}