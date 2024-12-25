package miyucomics.hexical.casting.special_handlers

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import miyucomics.hexical.casting.patterns.eval.OpNephthys

class NephthysSpecialHandler(private val depth: Int) : SpecialHandler {
	override fun act() = OpNephthys(depth)
	override fun getName() = "special.hexical.nephthys".asTranslatedComponent(depth).lightPurple

	class Factory : SpecialHandler.Factory<NephthysSpecialHandler> {
		override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): NephthysSpecialHandler? {
			val sig = pattern.anglesSignature()
			if (sig.startsWith("deaqqd")) {
				var depth = 1
				sig.substring(6).forEachIndexed { index, char ->
					if (char != "qe"[index % 2])
						return null
					depth += 1
				}
				return NephthysSpecialHandler(depth)
			} else {
				return null
			}
		}
	}
}