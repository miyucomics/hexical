package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota

class OpGetSleeping : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext) = listOf(BooleanIota(args.getLivingEntityButNotArmorStand(0, argc).isSleeping))
}