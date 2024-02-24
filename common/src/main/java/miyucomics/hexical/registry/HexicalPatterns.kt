package miyucomics.hexical.registry

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import miyucomics.hexical.Hexical
import miyucomics.hexical.casting.patterns.operators.eval.OpNephthys
import miyucomics.hexical.casting.patterns.operators.lamp.OpGetLampData
import miyucomics.hexical.casting.patterns.operators.lamp.OpGetMasterLampData
import miyucomics.hexical.casting.patterns.operators.lamp.OpIsUsingMasterLamp
import miyucomics.hexical.casting.patterns.spells.*
import net.minecraft.util.Identifier

object HexicalPatterns {
	private var PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private var PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	var NEPHTHYS_GAMBIT = register(HexPattern.fromAngles("deaqqdq", HexDir.SOUTH_EAST), "nephthys", OpNephthys);

	var CONJURE_ADVANCED_BLOCK: HexPattern = register(HexPattern.fromAngles("dee", HexDir.NORTH_WEST), "conjure_advanced_block", OpConjureAdvancedBlock())
	var CONFIGURE_BLOCK_BOUNCY: HexPattern = register(HexPattern.fromAngles("deeqa", HexDir.NORTH_WEST), "modify_block_bouncy", OpConfigureBlock("bouncy"))
	var CONFIGURE_BLOCK_EPHEMERAL: HexPattern = register(HexPattern.fromAngles("deewwaawd", HexDir.NORTH_WEST), "modify_block_ephemeral", OpConfigureBlock("ephemeral", 1))
	var CONFIGURE_BLOCK_INVISIBLE: HexPattern = register(HexPattern.fromAngles("deeqedeaqqqwqqq", HexDir.NORTH_WEST), "modify_block_invisible", OpConfigureBlock("invisible"))
	var CONFIGURE_BLOCK_VOLATILE: HexPattern = register(HexPattern.fromAngles("deewedeeeee", HexDir.NORTH_WEST), "modify_block_volatile", OpConfigureBlock("volatile"))

	var CHORUS_BLINK: HexPattern = register(HexPattern.fromAngles("aawqqqq", HexDir.SOUTH_EAST), "chorus_blink", OpChorusBlink())
	var PROGRAM_LAMP: HexPattern = register(HexPattern.fromAngles("wwqqqqq", HexDir.EAST), "program_lamp", OpProgramLamp())
	var PING: HexPattern = register(HexPattern.fromAngles("eweeewedqdeqqqqqwaeeee", HexDir.NORTH_EAST), "ping", OpPing())

	var CONJURE_STAFF: HexPattern = register(HexPattern.fromAngles("wweeeed", HexDir.NORTH_EAST), "conjure_staff", OpConjureStaff())

	var LAMP_POSITION: HexPattern = register(HexPattern.fromAngles("qwddeda", HexDir.SOUTH_WEST), "get_lamp_start_position", OpGetLampData(0))
	var LAMP_ROTATION: HexPattern = register(HexPattern.fromAngles("qwddedq", HexDir.SOUTH_WEST), "get_lamp_start_rotation", OpGetLampData(1))
	var LAMP_USE_TIME: HexPattern = register(HexPattern.fromAngles("qwddedw", HexDir.SOUTH_WEST), "get_lamp_use_time", OpGetLampData(2))

	var MASTER_LAMP_POSITION: HexPattern = register(HexPattern.fromAngles("qwddedaw", HexDir.SOUTH_WEST), "get_master_lamp_start_position", OpGetMasterLampData(0))
	var MASTER_LAMP_ROTATION: HexPattern = register(HexPattern.fromAngles("qwddedqw", HexDir.SOUTH_WEST), "get_master_lamp_start_rotation", OpGetMasterLampData(1))
	var MASTER_LAMP_USE_TIME: HexPattern = register(HexPattern.fromAngles("qwddedww", HexDir.SOUTH_WEST), "get_master_lamp_use_time", OpGetMasterLampData(2))

	var IS_USING_LAMP: HexPattern = register(HexPattern.fromAngles("qwddedww", HexDir.SOUTH_WEST), "is_using_lamp", OpIsUsingMasterLamp())
	var TERMINATE_LAMP: HexPattern = register(HexPattern.fromAngles("qwddedww", HexDir.SOUTH_WEST), "terminate_master_lamp", OpTerminateLamp())

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
