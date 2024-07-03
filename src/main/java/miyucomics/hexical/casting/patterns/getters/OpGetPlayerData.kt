package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.casting.iota.IdentifierIota
import net.minecraft.util.registry.Registry

class OpGetPlayerData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getPlayer(0, argc)
		return when (mode) {
			0 -> listOf(if (entity.mainHandStack.isEmpty) NullIota() else IdentifierIota(Registry.ITEM.getId(entity.mainHandStack.item)))
			1 -> listOf(if (entity.offHandStack.isEmpty) NullIota() else IdentifierIota(Registry.ITEM.getId(entity.offHandStack.item)))
			2 -> entity.hungerManager.foodLevel.asActionResult
			3 -> entity.hungerManager.saturationLevel.asActionResult
			else -> throw IllegalStateException()
		}
	}
}