package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries

class OpGetStatusEffectCategory : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) =
		when ((Registries.STATUS_EFFECT.get(args.getIdentifier(0, argc)) ?: throw MishapInvalidIota.of(args[0], 0, "status_effect_id")).category) {
			StatusEffectCategory.BENEFICIAL -> (1).asActionResult
			StatusEffectCategory.NEUTRAL -> (0).asActionResult
			StatusEffectCategory.HARMFUL -> (-1).asActionResult
			else -> throw IllegalStateException()
		}
}