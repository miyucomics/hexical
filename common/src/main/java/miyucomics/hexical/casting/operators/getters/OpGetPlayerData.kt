package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.iota.IdentifierIota
import net.minecraft.util.registry.Registry

class OpGetPlayerData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getPlayer(0, argc)
		return listOf(
			when (mode) {
				0 -> IdentifierIota(Registry.ITEM.getId(entity.mainHandStack.item))
				1 -> IdentifierIota(Registry.ITEM.getId(entity.offHandStack.item))
				2 -> DoubleIota(entity.hungerManager.foodLevel.toDouble())
				3 -> DoubleIota(entity.hungerManager.saturationLevel.toDouble())
				else -> NullIota()
			}
		)
	}
}