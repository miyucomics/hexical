package miyucomics.hexical.casting.patterns.eval

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.vm.FrameForEach
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs

object OpPollux : OpModifyThoth() {
	override fun updateFrame(frame: FrameForEach, stack: MutableList<Iota>) =
		frame.copy(data = SpellList.LList(frame.data + (stack.removeLastOrNull() ?: throw MishapNotEnoughArgs(1, 0))))
}