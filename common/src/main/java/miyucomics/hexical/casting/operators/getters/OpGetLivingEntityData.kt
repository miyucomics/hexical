package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.iota.IdentifierIota
import net.minecraft.util.registry.Registry

class OpGetLivingEntityData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		return listOf(
			when (mode) {
				0 -> BooleanIota(entity.isSleeping)
				1 -> BooleanIota(entity.isSprinting)
				2 -> {
					val list = mutableListOf<Iota>()
					for (effect in entity.statusEffects)
						list.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
					ListIota(list)
				}
				else -> NullIota()
			}
		)
	}
}