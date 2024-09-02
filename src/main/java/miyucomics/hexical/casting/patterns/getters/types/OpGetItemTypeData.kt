package miyucomics.hexical.casting.patterns.getters.types

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

class OpGetItemTypeData(private val process: (Item) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registry.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "item_id")
		return process(Registry.ITEM.get(id))
	}
}