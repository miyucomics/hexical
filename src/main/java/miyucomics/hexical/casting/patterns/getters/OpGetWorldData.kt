package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.iota.IdentifierIota

class OpGetWorldData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		return listOf(
			when (mode) {
				0 -> {
					if (ctx.world.isThundering)
						DoubleIota(2.0)
					else if (ctx.world.isRaining)
						DoubleIota(1.0)
					else
						DoubleIota(0.0)
				}
				1 -> IdentifierIota(ctx.world.registryKey.value)
				2 -> DoubleIota(ctx.world.time.toDouble() / 20)
				else -> throw IllegalStateException()
			}
		)
	}
}