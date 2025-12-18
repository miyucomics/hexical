package miyucomics.hexical.features.pattern_manipulation

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.arithmetic.operator.nextPositiveIntUnderInclusive
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.PATTERN

object OpPatternHead : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(PATTERN), IotaPredicate.ofType(DOUBLE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val it = iotas.iterator().withIndex()
		val pattern = it.nextPattern(arity)
		val count = it.nextPositiveIntUnderInclusive(pattern.angles.size, arity)
		return HexPattern(pattern.startDir, pattern.angles.take(count).toMutableList()).asActionResult
	}
}