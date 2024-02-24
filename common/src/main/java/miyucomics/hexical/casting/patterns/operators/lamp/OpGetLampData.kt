package miyucomics.hexical.casting.patterns.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.utils.vecFromNBT
import miyucomics.hexical.casting.mishaps.MishapNeedLamp
import miyucomics.hexical.registry.HexicalItems

class OpGetLampData(private val mode: Int) : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!(ctx.caster.activeItem.item == HexicalItems.LAMP_ITEM && ctx.source == CastingContext.CastSource.PACKAGED_HEX))
			throw MishapNeedLamp()
		val nbt = ctx.caster.activeItem.nbt ?: return listOf(NullIota())
		when (mode) {
			0 -> return listOf(Vec3Iota(vecFromNBT(nbt.getLongArray("position"))))
			1 -> return listOf(Vec3Iota(vecFromNBT(nbt.getLongArray("rotation"))))
			2 -> return listOf(Vec3Iota(vecFromNBT(nbt.getLongArray("velocity"))))
			3 -> return listOf(DoubleIota(ctx.world.time - nbt.getDouble("start_time")))
		}
		return listOf(NullIota())
	}
}