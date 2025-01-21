package miyucomics.hexical.casting.patterns.scrying.types

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iotas.getIdentifier
import net.minecraft.item.FoodComponent
import net.minecraft.registry.Registries

class OpGetFoodTypeData(private val process: (FoodComponent) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registries.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "food_id")
		return process(Registries.ITEM.get(id).foodComponent ?: throw MishapInvalidIota.of(args[0], 0, "food_id"))
	}
}