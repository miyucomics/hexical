package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import miyucomics.hexical.casting.iota.IdentifierIota
import net.minecraft.util.registry.Registry

class OpGetLivingEntityData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		return listOf(
			when (mode) {
				0 -> DoubleIota(entity.health.toDouble())
				1 -> DoubleIota(entity.maxHealth.toDouble())
				2 -> DoubleIota(entity.air.toDouble() / 20 + 1)
				3 -> DoubleIota(entity.maxAir.toDouble() / 20 + 1)
				4 -> BooleanIota(entity.isSleeping)
				5 -> BooleanIota(entity.isSprinting)
				6 -> {
					val list = mutableListOf<Iota>()
					for (effect in entity.statusEffects)
						list.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
					ListIota(list)
				}
				else -> throw IllegalStateException()
			}
		)
	}
}