package miyucomics.hexical.casting.patterns.scrying.types

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.item.Item
import net.minecraft.registry.Registries

class OpGetItemTypeData(private val process: (Item) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registries.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "item_id")
		return process(Registries.ITEM.get(id))
	}
}