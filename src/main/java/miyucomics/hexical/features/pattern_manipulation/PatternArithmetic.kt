package miyucomics.hexical.features.pattern_manipulation

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexAngle
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota

object PatternArithmetic : Arithmetic {
	val implementations: HashMap<HexPattern, Operator> = HashMap()

	init {
		implementations[Arithmetic.ABS] = patternIntoIota { DoubleIota(it.anglesSignature().length.toDouble() + 1) }
		implementations[Arithmetic.ADD] = patternPatternIntoIota { a, b -> PatternIota(addPatterns(a, b)) }
		implementations[Arithmetic.SUB] = patternPatternIntoIota { a, b -> DoubleIota(b.startDir.minus(a.startDir).ordinal.toDouble()) }
		implementations[Arithmetic.MUL] = patternNumberIntoIota { pat, rot -> PatternIota(pat.copy(startDir = pat.startDir * HexAngle.entries[((6 - rot) % 6).toInt()])) }
		implementations[Arithmetic.DIV] = patternNumberIntoIota { pat, rot -> PatternIota(pat.copy(startDir = pat.startDir * HexAngle.entries[(rot % 6).toInt()])) }
		implementations[Arithmetic.INDEX] = OpPatternHead
		implementations[Arithmetic.REMOVE] = OpPatternTail
	}

	override fun opTypes() = implementations.keys
	override fun arithName() = "hexical_pattern_manipulation"
	override fun getOperator(pattern: HexPattern) = implementations[pattern]

	private fun patternIntoIota(op: (HexPattern) -> Iota) =
		OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(PatternIota.TYPE))) { op((it as PatternIota).pattern) }
	private fun patternPatternIntoIota(op: (HexPattern, HexPattern) -> Iota) =
		OperatorBinary(IotaMultiPredicate.pair(IotaPredicate.ofType(PatternIota.TYPE), IotaPredicate.ofType(PatternIota.TYPE))) { a, b -> op((a as PatternIota).pattern, (b as PatternIota).pattern) }
	private fun patternNumberIntoIota(op: (HexPattern, Double) -> Iota) =
		OperatorBinary(IotaMultiPredicate.pair(IotaPredicate.ofType(PatternIota.TYPE), IotaPredicate.ofType(DoubleIota.TYPE))) { pattern, number -> op((pattern as PatternIota).pattern, (number as DoubleIota).double) }

	fun addPatterns(a: HexPattern, b: HexPattern) = HexPattern(a.startDir, a.angles.plus(b.startDir.angleFrom(a.finalDir())).plus(b.angles).toMutableList())
}

fun Iterator<IndexedValue<Iota>>.nextPattern(argc: Int = 0): HexPattern {
	val (idx, x) = this.next()
	if (x is PatternIota) {
		return x.pattern
	} else {
		throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "pattern")
	}
}