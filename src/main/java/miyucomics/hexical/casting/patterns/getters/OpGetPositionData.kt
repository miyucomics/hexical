package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.iota.asActionResult

class OpGetPositionData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		return when (mode) {
			0 -> ctx.world.getLightLevel(position).asActionResult
			1 -> ctx.world.getBiome(position).key.get().value.asActionResult()
			else -> throw IllegalStateException()
		}
	}
}