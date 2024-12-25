package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.vm.FrameForEach
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs

object OpCastor : OpModifyThoth() {
	override fun updateFrame(frame: FrameForEach, stack: MutableList<Iota>): FrameForEach {
		val iota = stack.removeLastOrNull() ?: throw MishapNotEnoughArgs(1, 0)
		return frame.copy(data = SpellList.LPair(iota, frame.data))
	}
}