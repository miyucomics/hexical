package miyucomics.hexical.casting.patterns.getters.types

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.item.FoodComponent
import net.minecraft.util.registry.Registry

class OpGetFoodTypeData(private val process: (FoodComponent) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registry.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "food_id")
		val food = Registry.ITEM.get(id).foodComponent ?: throw MishapInvalidIota.of(args[0], 0, "food_id")
		return process(food)
	}
}