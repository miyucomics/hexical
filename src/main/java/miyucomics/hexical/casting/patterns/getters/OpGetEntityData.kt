package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota

class OpGetEntityData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getEntity(0, argc)
		return when (mode) {
			0 -> entity.isOnFire.asActionResult
			1 -> (entity.fireTicks.toDouble() / 20).asActionResult
			2 -> entity.isWet.asActionResult
			else -> throw IllegalStateException()
		}
	}
}