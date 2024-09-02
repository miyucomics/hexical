package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.spell.SpellList
import at.petrak.hexcasting.api.spell.casting.eval.FrameForEach
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

// prepend
object OpCastor : OpModifyThoth() {
	override fun updateFrame(frame: FrameForEach, stack: MutableList<Iota>): FrameForEach {
		val iota = stack.removeLastOrNull() ?: throw MishapNotEnoughArgs(1, 0)
		return frame.copy(
			data = SpellList.LPair(iota, frame.data)
		)
	}
}