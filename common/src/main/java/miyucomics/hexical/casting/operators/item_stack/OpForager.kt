package miyucomics.hexical.casting.operators.item_stack

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getItemEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.iota.ItemStackIota

class OpForager : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getItemEntity(0, argc)
		return listOf(ItemStackIota(entity.stack.copy()))
	}
}