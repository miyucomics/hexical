package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.state.KeybindData

class OpGetKeybind(private val key: String) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!KeybindData.active.containsKey(ctx.caster.uuid))
			return (0).asActionResult
		return KeybindData.duration[ctx.caster.uuid]!!.getOrDefault(key, 0.0).asActionResult
	}
}