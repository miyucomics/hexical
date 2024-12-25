package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.Registries

class OpGetStatusEffectInstanceData(private val process: (StatusEffectInstance) -> List<Iota>) : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		env.assertEntityInRange(entity)
		val effect = args.getIdentifier(1, argc)
		if (!Registries.STATUS_EFFECT.containsId(effect))
			throw MishapInvalidIota.of(args[1], 1, "status_effect")
		return process(entity.getStatusEffect(Registries.STATUS_EFFECT.get(effect)) ?: return listOf(NullIota()))
	}
}