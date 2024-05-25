package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.iota.getIdentifier
import net.minecraft.util.registry.Registry

class OpGetStatusEffectInstanceData(private val mode: Int) : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		ctx.assertEntityInRange(entity)
		val effect = args.getIdentifier(1, argc)
		if (!Registry.STATUS_EFFECT.containsId(effect))
			throw MishapInvalidIota.of(args[1], 1, "status_effect")
		val instance = entity.getStatusEffect(Registry.STATUS_EFFECT.get(effect)) ?: return listOf(NullIota())
		return listOf(
			when (mode) {
				0 -> DoubleIota(instance.amplifier.toDouble())
				1 -> DoubleIota(instance.duration.toDouble())
				else -> NullIota()
			}
		)
	}
}