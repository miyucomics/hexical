package miyucomics.hexical.casting.patterns.operators.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import miyucomics.hexical.casting.mishaps.MishapNeedLamp
import miyucomics.hexical.registry.HexicalItems
import at.petrak.hexcasting.api.utils.vecFromNBT

class OpGetLampStartData(private val mode: Int) : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!(ctx.caster.activeItem.item == HexicalItems.LAMP_ITEM.asItem() && ctx.source == CastingContext.CastSource.PACKAGED_HEX))
			throw MishapNeedLamp()

		val rodNbt = ctx.caster.activeItem.nbt ?: return listOf(NullIota())
		when (mode) {
			1 -> return listOf(Vec3Iota(vecFromNBT(rodNbt.getLongArray("startPosition"))))
			2 -> return listOf(Vec3Iota(vecFromNBT(rodNbt.getLongArray("startRotation"))))
			3 -> return listOf(DoubleIota(ctx.world.time - rodNbt.getDouble("startTime")))
		}

		return listOf(NullIota())
	}
}