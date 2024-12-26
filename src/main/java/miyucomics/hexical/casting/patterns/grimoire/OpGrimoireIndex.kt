package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import miyucomics.hexical.inits.HexicalItems

class OpGrimoireIndex : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.GRIMOIRE_ITEM) }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "grimoire")
		val result = mutableListOf<PatternIota>()
		for (pattern in stack.stack.orCreateNbt.getOrCreateCompound("patterns").keys)
			result.add(PatternIota(HexPattern.fromAngles(pattern, HexDir.WEST)))
		return listOf(ListIota(result.toList()))
	}
}