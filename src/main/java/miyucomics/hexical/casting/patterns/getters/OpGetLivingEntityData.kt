package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.iota.IdentifierIota
import net.minecraft.util.registry.Registry

class OpGetLivingEntityData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getLivingEntityButNotArmorStand(0, argc)
		return when (mode) {
			0 -> entity.health.asActionResult
			1 -> entity.maxHealth.asActionResult
			2 -> (entity.air.toDouble() / 20 + 1).asActionResult
			3 -> (entity.maxAir.toDouble() / 20 + 1).asActionResult
			4 -> entity.isSleeping.asActionResult
			5 -> entity.isSprinting.asActionResult
			6 -> {
				val list = mutableListOf<Iota>()
				for (effect in entity.statusEffects)
					list.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
				list.asActionResult
			}
			else -> throw IllegalStateException()
		}
	}
}