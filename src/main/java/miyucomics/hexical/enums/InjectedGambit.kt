package miyucomics.hexical.enums

import at.petrak.hexcasting.api.spell.SpellList
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.Iota

enum class InjectedGambit {
	THEMIS,
	SISYPHUS,
	NONE
}

interface FakeGambit {
	fun breakDownwards(baseStack: List<Iota>, accumulator: MutableList<Iota>): Pair<Boolean, List<Iota>>
	fun evaluate(continuation: SpellContinuation, harness: CastingHarness, data: SpellList, code: SpellList, baseStack: List<Iota>?, accumulator: MutableList<Iota>): CastingHarness.CastResult
}