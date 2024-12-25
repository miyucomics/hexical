package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.util.registry.Registry

class OpGetStatusEffectInstanceData(private val process: (StatusEffectInstance) -> List<Iota>) : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		ctx.assertEntityInRange(entity)
		val effect = args.getIdentifier(1, argc)
		if (!Registry.STATUS_EFFECT.containsId(effect))
			throw MishapInvalidIota.of(args[1], 1, "status_effect")
		return process(entity.getStatusEffect(Registry.STATUS_EFFECT.get(effect)) ?: return listOf(NullIota()))
	}
}