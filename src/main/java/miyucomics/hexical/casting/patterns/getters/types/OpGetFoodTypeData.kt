package miyucomics.hexical.casting.patterns.getters.types

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.util.registry.Registry

class OpGetFoodTypeData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registry.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "food_id")
		val food = Registry.ITEM.get(id).foodComponent?: throw MishapInvalidIota.of(args[0], 0, "food_id")
		return when (mode) {
			0 -> food.hunger.asActionResult
			1 -> food.saturationModifier.asActionResult
			2 -> food.isMeat.asActionResult
			3 -> food.isSnack.asActionResult
			else -> throw IllegalStateException()
		}
	}
}