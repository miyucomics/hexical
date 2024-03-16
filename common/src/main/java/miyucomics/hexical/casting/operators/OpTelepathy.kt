package miyucomics.hexical.casting.operators

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.persistent_state.TelepathyData
import net.minecraft.text.Text

class OpTelepathy : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val caster = ctx.caster.uuid
		if (!TelepathyData.active.contains(caster))
			return listOf(DoubleIota(-1.0))
		if (!TelepathyData.active[caster]!!)
			return listOf(DoubleIota(-1.0))
		return listOf(DoubleIota(TelepathyData.timer[caster]!!.toDouble()))
	}
}