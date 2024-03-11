package miyucomics.hexical.registry

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import miyucomics.hexical.Hexical
import miyucomics.hexical.casting.operators.*
import miyucomics.hexical.casting.operators.eval.OpNephthys
import miyucomics.hexical.casting.operators.grimoire.*
import miyucomics.hexical.casting.operators.lamp.OpGetArchLampData
import miyucomics.hexical.casting.operators.lamp.OpGetLampData
import miyucomics.hexical.casting.operators.lamp.OpIsUsingArchLamp
import miyucomics.hexical.casting.spells.*
import miyucomics.hexical.casting.spells.lamp.OpProgramLamp
import miyucomics.hexical.casting.spells.lamp.OpSetArchLampStorage
import miyucomics.hexical.casting.spells.lamp.OpTerminateArchLamp
import net.minecraft.util.Identifier

object HexicalPatterns {
	private val PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private val PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	val NEPHTHYS_GAMBIT = register(HexPattern.fromAngles("deaqqdq", HexDir.SOUTH_EAST), "nephthys", OpNephthys);

	val CONJURE_ADVANCED_BLOCK: HexPattern = register(HexPattern.fromAngles("dee", HexDir.NORTH_WEST), "conjure_mage_block", OpConjureMageBlock())
	val MODIFY_BLOCK_BOUNCY: HexPattern = register(HexPattern.fromAngles("deeqa", HexDir.NORTH_WEST), "modify_block_bouncy", OpModifyMageBlock("bouncy"))
	val MODIFY_BLOCK_EPHEMERAL: HexPattern = register(HexPattern.fromAngles("deewwaawd", HexDir.NORTH_WEST), "modify_block_ephemeral", OpModifyMageBlock("ephemeral", 1))
	val MODIFY_BLOCK_INVISIBLE: HexPattern = register(HexPattern.fromAngles("deeqedeaqqqwqqq", HexDir.NORTH_WEST), "modify_block_invisible", OpModifyMageBlock("invisible"))
	val MODIFY_BLOCK_REPLACEABLE: HexPattern = register(HexPattern.fromAngles("deewqaqqqqq", HexDir.NORTH_WEST), "modify_block_replaceable", OpModifyMageBlock("replaceable"))
	val MODIFY_BLOCK_VOLATILE: HexPattern = register(HexPattern.fromAngles("deewedeeeee", HexDir.NORTH_WEST), "modify_block_volatile", OpModifyMageBlock("volatile"))

	val CHORUS_BLINK: HexPattern = register(HexPattern.fromAngles("aawqqqq", HexDir.SOUTH_EAST), "chorus_blink", OpChorusBlink())

	val CONJURE_STAFF: HexPattern = register(HexPattern.fromAngles("wwwwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST), "conjure_staff", OpConjureStaff())
	val CONJURE_SPECK: HexPattern = register(HexPattern.fromAngles("eeeee", HexDir.EAST), "conjure_speck", OpConjureSpeck())

	val ENTITY_BURNING: HexPattern = register(HexPattern.fromAngles("qqwaqda", HexDir.EAST), "is_burning", OpGetEntityBurning())
	val ENTITY_BURNING_TIME: HexPattern = register(HexPattern.fromAngles("eewdead", HexDir.WEST), "burning_time", OpGetEntityBurningTime())
	val ENTITY_WET: HexPattern = register(HexPattern.fromAngles("qqqqwaadq", HexDir.SOUTH_WEST), "is_wet", OpGetEntityWet())
	val PLAYER_SPRINTING: HexPattern = register(HexPattern.fromAngles("eaq", HexDir.WEST), "is_sprinting", OpGetPlayerSprinting())

	val WRITE_TO_GRIMOIRE: HexPattern = register(HexPattern.fromAngles("aqwqaeaqa", HexDir.WEST), "write_grimoire", OpGrimoireWrite())
	val ERASE_GRIMOIRE: HexPattern = register(HexPattern.fromAngles("aqwqaqded", HexDir.EAST), "erase_grimoire", OpGrimoireErase())
	val INDEX_GRIMOIRE: HexPattern = register(HexPattern.fromAngles("aqaeaqwqa", HexDir.SOUTH_EAST), "index_grimoire", OpGrimoireIndex())
	val RESTRICT_GRIMOIRE: HexPattern = register(HexPattern.fromAngles("dedqdewed", HexDir.SOUTH_WEST), "restrict_grimoire", OpGrimoireRestrict())
	val QUERY_GRIMOIRE: HexPattern = register(HexPattern.fromAngles("aqaedewed", HexDir.NORTH_WEST), "query_grimoire", OpGrimoireQuery())

	val PROGRAM_LAMP: HexPattern = register(HexPattern.fromAngles("wwqqqqq", HexDir.EAST), "program_lamp", OpProgramLamp())
	val LAMP_POSITION: HexPattern = register(HexPattern.fromAngles("qwddedqdd", HexDir.SOUTH_WEST), "get_lamp_position", OpGetLampData(0))
	val LAMP_ROTATION: HexPattern = register(HexPattern.fromAngles("qwddedadw", HexDir.SOUTH_WEST), "get_lamp_rotation", OpGetLampData(1))
	val LAMP_VELOCITY: HexPattern = register(HexPattern.fromAngles("qwddedqew", HexDir.SOUTH_WEST), "get_lamp_velocity", OpGetLampData(2))
	val LAMP_USE_TIME: HexPattern = register(HexPattern.fromAngles("qwddedqwddwa", HexDir.SOUTH_WEST), "get_lamp_use_time", OpGetLampData(3))
	val ARCH_LAMP_POSITION: HexPattern = register(HexPattern.fromAngles("qaqwddedqdd", HexDir.NORTH_EAST), "get_arch_lamp_position", OpGetArchLampData(0))
	val ARCH_LAMP_ROTATION: HexPattern = register(HexPattern.fromAngles("qaqwddedadw", HexDir.NORTH_EAST), "get_arch_lamp_rotation", OpGetArchLampData(1))
	val ARCH_LAMP_VELOCITY: HexPattern = register(HexPattern.fromAngles("qaqwddedqew", HexDir.NORTH_EAST), "get_arch_lamp_velocity", OpGetArchLampData(2))
	val ARCH_LAMP_USE_TIME: HexPattern = register(HexPattern.fromAngles("qaqwddedqwddwa", HexDir.NORTH_EAST), "get_arch_lamp_use_time", OpGetArchLampData(3))
	val ARCH_LAMP_STORAGE: HexPattern = register(HexPattern.fromAngles("qaqwddedqwaqqqqq", HexDir.NORTH_EAST), "get_arch_lamp_storage", OpGetArchLampData(4))
	val IS_USING_LAMP: HexPattern = register(HexPattern.fromAngles("qaqwddedqeed", HexDir.NORTH_EAST), "is_using_lamp", OpIsUsingArchLamp())
	val TERMINATE_LAMP: HexPattern = register(HexPattern.fromAngles("qaqwddedwaqdee", HexDir.NORTH_EAST), "terminate_arch_lamp", OpTerminateArchLamp())
	val SET_LAMP_STORAGE: HexPattern = register(HexPattern.fromAngles("qaqwddedqedeeeee", HexDir.NORTH_EAST), "set_arch_lamp_storage", OpSetArchLampStorage())

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
