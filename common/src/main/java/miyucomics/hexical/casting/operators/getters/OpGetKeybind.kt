package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.state.KeybindData

class OpGetKeybind(private val key: String) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!KeybindData.lookup.containsKey(ctx.caster.uuid))
			return listOf(BooleanIota(false))
		return listOf(BooleanIota(KeybindData.lookup[ctx.caster.uuid]!!.getOrDefault(key, false)))
	}
}