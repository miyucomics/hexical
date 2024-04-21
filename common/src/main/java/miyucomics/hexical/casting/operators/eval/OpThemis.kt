package miyucomics.hexical.casting.operators.eval

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.evaluatable
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.getPattern
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import miyucomics.hexical.casting.mishaps.ThemisMishap

class OpThemis : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val toSort = args.getList(0, argc).toMutableList()
		evaluatable(args[1], 1)
		val executable: List<Iota> = when (args[1]) {
			is ListIota -> args.getList(1, argc).toMutableList()
			is PatternIota -> mutableListOf(PatternIota(args.getPattern(1, argc)))
			else -> listOf(NullIota())
		}
		toSort.sortBy { judge(it, executable, ctx) }
		return listOf(ListIota(toSort))
	}

	private fun judge(iota: Iota, judgement: List<Iota>, ctx: CastingContext): Double {
		val tempCtx = CastingContext(ctx.caster, ctx.castingHand, CastingContext.CastSource.PACKAGED_HEX)
		val harness = CastingHarness(tempCtx)
		harness.stack = mutableListOf(iota)
		harness.executeIotas(judgement, ctx.world)
		if (harness.stack.size == 0)
			throw ThemisMishap()
		val top = harness.stack.last()
		if (top !is DoubleIota)
			throw ThemisMishap()
		return top.double
	}
}