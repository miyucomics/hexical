package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.iota.getIdentifier
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.util.registry.Registry

class OpGetStatusEffectCategory : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val effect = args.getIdentifier(0, argc)
		if (!Registry.STATUS_EFFECT.containsId(effect))
			throw MishapInvalidIota.of(args[0], 0, "status_effect")
		return listOf(when (Registry.STATUS_EFFECT.get(effect)!!.category) {
			StatusEffectCategory.BENEFICIAL -> DoubleIota(1.0)
			StatusEffectCategory.NEUTRAL -> DoubleIota(0.0)
			StatusEffectCategory.HARMFUL -> DoubleIota(-1.0)
			else -> NullIota()
		})
	}
}