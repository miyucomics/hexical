package miyucomics.hexical.casting.operators.status

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import miyucomics.hexical.iota.IdentifierIota
import net.minecraft.util.registry.Registry

class OpGetStatusEffects : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		val list = mutableListOf<Iota>()
		for (effect in entity.statusEffects)
			list.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
		return listOf(ListIota(list))
	}
}