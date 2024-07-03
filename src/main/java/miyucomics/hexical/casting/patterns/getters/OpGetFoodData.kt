package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota

class OpGetFoodData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = args.getItemStack(0, argc)
		if (!stack.isFood)
			throw MishapInvalidIota.of(args[0], 0, "food")
		val foodComponent = stack.item.foodComponent!!
		return when (mode) {
			0 -> foodComponent.hunger.asActionResult
			1 -> foodComponent.saturationModifier.asActionResult
			2 -> foodComponent.isMeat.asActionResult
			3 -> foodComponent.isSnack.asActionResult
			else -> throw IllegalStateException()
		}
	}
}