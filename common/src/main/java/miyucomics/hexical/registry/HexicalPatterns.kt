package miyucomics.hexical.registry

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.common.casting.operators.spells.OpMakePackagedSpell
import miyucomics.hexical.Hexical
import miyucomics.hexical.casting.patterns.spells.OpChorusBlink
import miyucomics.hexical.casting.patterns.spells.OpConjureBouncyBlock
import miyucomics.hexical.casting.patterns.spells.OpPing
import miyucomics.hexical.casting.patterns.spells.OpProgramLamp
import miyucomics.hexical.items.LampItem
import net.minecraft.util.Identifier

object HexicalPatterns {
	private var PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private var PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	var CHORUS_BLINK: HexPattern = register(HexPattern.fromAngles("aawqqqq", HexDir.SOUTH_EAST), "chorus_blink", OpChorusBlink())
	var CONJURE_BOUNCY_BLOCK: HexPattern = register(HexPattern.fromAngles("dee", HexDir.NORTH_WEST), "conjure_bouncy_block", OpConjureBouncyBlock())
	var PROGRAM_LAMP: HexPattern = register(HexPattern.fromAngles("wwqqqqq", HexDir.EAST), "program_lamp", OpProgramLamp())
	var PING_SPELL: HexPattern = register(HexPattern.fromAngles("eweeewedqdeqqqqqwaeeee", HexDir.NORTH_EAST), "ping", OpPing())

	@JvmStatic
	fun init() {
		try {
			for ((first, second, third) in PATTERNS)
				PatternRegistry.mapPattern(first, second, third)
			for ((first, second, third) in PER_WORLD_PATTERNS)
				PatternRegistry.mapPattern(first, second, third, true)
		} catch (e: PatternRegistry.RegisterPatternException) {
			e.printStackTrace()
		}
	}

	private fun register(pattern: HexPattern, name: String, action: Action): HexPattern {
		val triple = Triple(pattern, Hexical.id(name), action)
		PATTERNS.add(triple)
		return pattern
	}

	private fun registerPerWorld(pattern: HexPattern, name: String, action: Action): HexPattern {
		val triple = Triple(pattern, Hexical.id(name), action)
		PER_WORLD_PATTERNS.add(triple)
		return pattern
	}
}