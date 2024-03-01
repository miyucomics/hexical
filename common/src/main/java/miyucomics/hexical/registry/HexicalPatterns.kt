package miyucomics.hexical.registry

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import miyucomics.hexical.Hexical
import miyucomics.hexical.casting.patterns.operators.eval.OpNephthys
import miyucomics.hexical.casting.patterns.operators.lamp.OpGetArchLampData
import miyucomics.hexical.casting.patterns.operators.lamp.OpGetLampData
import miyucomics.hexical.casting.patterns.operators.lamp.OpGetPlayerSprinting
import miyucomics.hexical.casting.patterns.operators.lamp.OpIsUsingArchLamp
import miyucomics.hexical.casting.patterns.spells.*
import net.minecraft.util.Identifier

object HexicalPatterns {
	private var PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private var PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	var NEPHTHYS_GAMBIT = register(HexPattern.fromAngles("deaqqdq", HexDir.SOUTH_EAST), "nephthys", OpNephthys);

	var CONJURE_ADVANCED_BLOCK: HexPattern = register(HexPattern.fromAngles("dee", HexDir.NORTH_WEST), "conjure_mage_block", OpConjureMageBlock())
	var CONFIGURE_BLOCK_BOUNCY: HexPattern = register(HexPattern.fromAngles("deeqa", HexDir.NORTH_WEST), "modify_block_bouncy", OpConfigureMageBlock("bouncy"))
	var CONFIGURE_BLOCK_EPHEMERAL: HexPattern = register(HexPattern.fromAngles("deewwaawd", HexDir.NORTH_WEST), "modify_block_ephemeral", OpConfigureMageBlock("ephemeral", 1))
	var CONFIGURE_BLOCK_INVISIBLE: HexPattern = register(HexPattern.fromAngles("deeqedeaqqqwqqq", HexDir.NORTH_WEST), "modify_block_invisible", OpConfigureMageBlock("invisible"))
	var CONFIGURE_BLOCK_REPLACEABLE: HexPattern = register(HexPattern.fromAngles("deewqaqqqqq", HexDir.NORTH_WEST), "modify_block_replaceable", OpConfigureMageBlock("replaceable"))
	var CONFIGURE_BLOCK_VOLATILE: HexPattern = register(HexPattern.fromAngles("deewedeeeee", HexDir.NORTH_WEST), "modify_block_volatile", OpConfigureMageBlock("volatile"))

	var CHORUS_BLINK: HexPattern = register(HexPattern.fromAngles("aawqqqq", HexDir.SOUTH_EAST), "chorus_blink", OpChorusBlink())
	var PROGRAM_LAMP: HexPattern = register(HexPattern.fromAngles("wwqqqqq", HexDir.EAST), "program_lamp", OpProgramLamp())

	var CONJURE_STAFF: HexPattern = register(HexPattern.fromAngles("wweeeed", HexDir.NORTH_EAST), "conjure_staff", OpConjureStaff())
	var PLAYER_SPRINTING: HexPattern = register(HexPattern.fromAngles("eaq", HexDir.WEST), "is_sprinting", OpGetPlayerSprinting())

	var LAMP_POSITION: HexPattern = register(HexPattern.fromAngles("qwddedqdd", HexDir.SOUTH_WEST), "get_lamp_position", OpGetLampData(0))
	var LAMP_ROTATION: HexPattern = register(HexPattern.fromAngles("qwddedadw", HexDir.SOUTH_WEST), "get_lamp_rotation", OpGetLampData(1))
	var LAMP_VELOCITY: HexPattern = register(HexPattern.fromAngles("qwddedqew", HexDir.SOUTH_WEST), "get_lamp_velocity", OpGetLampData(2))
	var LAMP_USE_TIME: HexPattern = register(HexPattern.fromAngles("qwddedqwddwa", HexDir.SOUTH_WEST), "get_lamp_use_time", OpGetLampData(3))
	var ARCH_LAMP_POSITION: HexPattern = register(HexPattern.fromAngles("qaqwddedqdd", HexDir.NORTH_EAST), "get_arch_lamp_position", OpGetArchLampData(0))
	var ARCH_LAMP_ROTATION: HexPattern = register(HexPattern.fromAngles("qaqwddedadw", HexDir.NORTH_EAST), "get_arch_lamp_rotation", OpGetArchLampData(1))
	var ARCH_LAMP_VELOCITY: HexPattern = register(HexPattern.fromAngles("qaqwddedqew", HexDir.NORTH_EAST), "get_arch_lamp_velocity", OpGetArchLampData(2))
	var ARCH_LAMP_USE_TIME: HexPattern = register(HexPattern.fromAngles("qaqwddedqwddwa", HexDir.NORTH_EAST), "get_arch_lamp_use_time", OpGetArchLampData(3))
	var ARCH_LAMP_STORAGE: HexPattern = register(HexPattern.fromAngles("qaqwddedqwaqqqqq", HexDir.NORTH_EAST), "get_arch_lamp_storage", OpGetArchLampData(4))
	var IS_USING_LAMP: HexPattern = register(HexPattern.fromAngles("qaqwddedqeed", HexDir.NORTH_EAST), "is_using_lamp", OpIsUsingArchLamp())
	var TERMINATE_LAMP: HexPattern = register(HexPattern.fromAngles("qaqwddedwaqdee", HexDir.NORTH_EAST), "terminate_arch_lamp", OpTerminateArchLamp())
	var SET_LAMP_STORAGE: HexPattern = register(HexPattern.fromAngles("qaqwddedqedeeeee", HexDir.NORTH_EAST), "set_arch_lamp_storage", OpSetArchLampStorage())

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