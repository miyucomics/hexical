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
import miyucomics.hexical.casting.operators.lamp.OpGetArchLampMedia
import miyucomics.hexical.casting.operators.lamp.OpGetLampData
import miyucomics.hexical.casting.operators.lamp.OpIsUsingArchLamp
import miyucomics.hexical.casting.spells.*
import miyucomics.hexical.casting.spells.lamp.OpProgramLamp
import miyucomics.hexical.casting.spells.lamp.OpSetArchLampStorage
import miyucomics.hexical.casting.spells.lamp.OpTerminateArchLamp
import miyucomics.hexical.casting.spells.specks.OpConjureSpeck
import net.minecraft.util.Identifier

object HexicalPatterns {
	private val PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private val PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	@JvmStatic
	fun init() {
		register(HexPattern.fromAngles("deaqqdq", HexDir.SOUTH_EAST), "nephthys", OpNephthys)

		register(HexPattern.fromAngles("dee", HexDir.NORTH_WEST), "conjure_mage_block", OpConjureMageBlock())
		register(HexPattern.fromAngles("deeqa", HexDir.NORTH_WEST), "modify_block_bouncy", OpModifyMageBlock("bouncy"))
		register(HexPattern.fromAngles("deewwaawd", HexDir.NORTH_WEST), "modify_block_ephemeral", OpModifyMageBlock("ephemeral", 1))
		register(HexPattern.fromAngles("deeqedeaqqqwqqq", HexDir.NORTH_WEST), "modify_block_invisible", OpModifyMageBlock("invisible"))
		register(HexPattern.fromAngles("deewqaqqqqq", HexDir.NORTH_WEST), "modify_block_replaceable", OpModifyMageBlock("replaceable"))
		register(HexPattern.fromAngles("deewedeeeee", HexDir.NORTH_WEST), "modify_block_volatile", OpModifyMageBlock("volatile"))

		register(HexPattern.fromAngles("aawqqqq", HexDir.SOUTH_EAST), "chorus_blink", OpChorusBlink())

		register(HexPattern.fromAngles("deedqad", HexDir.WEST), "get_weather", OpGetWeather())

		register(HexPattern.fromAngles("wwwwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST), "conjure_staff", OpConjureStaff())
		register(HexPattern.fromAngles("eeeee", HexDir.EAST), "conjure_speck", OpConjureSpeck())

		register(HexPattern.fromAngles("qqwaqda", HexDir.EAST), "is_burning", OpGetEntityBurning())
		register(HexPattern.fromAngles("eewdead", HexDir.WEST), "burning_time", OpGetEntityBurningTime())
		register(HexPattern.fromAngles("qqqqwaadq", HexDir.SOUTH_WEST), "is_wet", OpGetEntityWet())
		register(HexPattern.fromAngles("eaq", HexDir.WEST), "is_sprinting", OpGetPlayerSprinting())
		register(HexPattern.fromAngles("qaqqqqded", HexDir.NORTH_EAST), "telepathy", OpTelepathy())

		register(HexPattern.fromAngles("aqwqaeaqa", HexDir.WEST), "write_grimoire", OpGrimoireWrite())
		register(HexPattern.fromAngles("aqwqaqded", HexDir.WEST), "erase_grimoire", OpGrimoireErase())
		register(HexPattern.fromAngles("aqaeaqwqa", HexDir.SOUTH_EAST), "index_grimoire", OpGrimoireIndex())
		register(HexPattern.fromAngles("dedqdewed", HexDir.SOUTH_WEST), "restrict_grimoire", OpGrimoireRestrict())
		register(HexPattern.fromAngles("aqaedewed", HexDir.NORTH_WEST), "query_grimoire", OpGrimoireQuery())

		register(HexPattern.fromAngles("wwqqqqq", HexDir.EAST), "program_lamp", OpProgramLamp())
		register(HexPattern.fromAngles("qwddedqdd", HexDir.SOUTH_WEST), "get_lamp_position", OpGetLampData(0))
		register(HexPattern.fromAngles("qwddedadw", HexDir.SOUTH_WEST), "get_lamp_rotation", OpGetLampData(1))
		register(HexPattern.fromAngles("qwddedqew", HexDir.SOUTH_WEST), "get_lamp_velocity", OpGetLampData(2))
		register(HexPattern.fromAngles("qwddedqwddwa", HexDir.SOUTH_WEST), "get_lamp_use_time", OpGetLampData(3))
		register(HexPattern.fromAngles("qwddedqwwaqqqqqeaqeaeqqqeaeq", HexDir.SOUTH_WEST), "get_lamp_media", OpGetLampData(4))
		register(HexPattern.fromAngles("qaqwddedqdd", HexDir.NORTH_EAST), "get_arch_lamp_position", OpGetArchLampData(0))
		register(HexPattern.fromAngles("qaqwddedadw", HexDir.NORTH_EAST), "get_arch_lamp_rotation", OpGetArchLampData(1))
		register(HexPattern.fromAngles("qaqwddedqew", HexDir.NORTH_EAST), "get_arch_lamp_velocity", OpGetArchLampData(2))
		register(HexPattern.fromAngles("qaqwddedqwddwa", HexDir.NORTH_EAST), "get_arch_lamp_use_time", OpGetArchLampData(3))
		register(HexPattern.fromAngles("qaqwddedqwaqqqqq", HexDir.NORTH_EAST), "get_arch_lamp_storage", OpGetArchLampData(4))
		register(HexPattern.fromAngles("qaqwddedqwwaqqqqqeaqeaeqqqeaeq", HexDir.NORTH_EAST), "get_arch_lamp_media", OpGetArchLampMedia())
		register(HexPattern.fromAngles("qaqwddedqeed", HexDir.NORTH_EAST), "is_using_lamp", OpIsUsingArchLamp())
		register(HexPattern.fromAngles("qaqwddedwaqdee", HexDir.NORTH_EAST), "terminate_arch_lamp", OpTerminateArchLamp())
		register(HexPattern.fromAngles("qaqwddedqedeeeee", HexDir.NORTH_EAST), "set_arch_lamp_storage", OpSetArchLampStorage())

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
