package miyucomics.hexical.casting.patterns.getters.misc

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.util.registry.Registry

class OpGetStatusEffectCategory : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val effect = args.getIdentifier(0, argc)
		if (!Registry.STATUS_EFFECT.containsId(effect))
			throw MishapInvalidIota.of(args[0], 0, "status_effect_id")
		return when (Registry.STATUS_EFFECT.get(effect)!!.category) {
			StatusEffectCategory.BENEFICIAL -> (1).asActionResult
			StatusEffectCategory.NEUTRAL -> (0).asActionResult
			StatusEffectCategory.HARMFUL -> (-1).asActionResult
			else -> throw IllegalStateException()
		}
	}
}