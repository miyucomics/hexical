package miyucomics.hexical.registry

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntitiesBy
import miyucomics.hexical.Hexical
import miyucomics.hexical.casting.operators.OpGetTelepathy
import miyucomics.hexical.casting.operators.eval.OpNephthys
import miyucomics.hexical.casting.operators.getters.*
import miyucomics.hexical.casting.operators.grimoire.*
import miyucomics.hexical.casting.operators.identifier.OpIdentify
import miyucomics.hexical.casting.operators.identifier.OpRecognize
import miyucomics.hexical.casting.operators.lamp.*
import miyucomics.hexical.casting.spells.OpChorusBlink
import miyucomics.hexical.casting.spells.OpConjureMageBlock
import miyucomics.hexical.casting.spells.OpConjureStaff
import miyucomics.hexical.casting.spells.OpModifyMageBlock
import miyucomics.hexical.casting.spells.lamp.OpEducateGenie
import miyucomics.hexical.casting.spells.lamp.OpMakeGenie
import miyucomics.hexical.casting.spells.lamp.OpSetArchLampStorage
import miyucomics.hexical.casting.spells.lamp.OpTerminateArchLamp
import miyucomics.hexical.casting.spells.specks.*
import miyucomics.hexical.casting.spells.telepathy.OpSendTelepathy
import miyucomics.hexical.casting.spells.telepathy.OpShoutTelepathy
import miyucomics.hexical.casting.spells.telepathy.OpSoundPing
import miyucomics.hexical.entities.SpeckEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

object HexicalPatterns {
	private val PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private val PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	@JvmStatic
	fun init() {
		register(HexPattern.fromAngles("deaqqdq", HexDir.SOUTH_EAST), "nephthys", OpNephthys)

		register(HexPattern.fromAngles("qqqqqe", HexDir.NORTH_EAST), "identify", OpIdentify())
		register(HexPattern.fromAngles("eeeeeq", HexDir.WEST), "recognize", OpRecognize())

		register(HexPattern.fromAngles("dee", HexDir.NORTH_WEST), "conjure_mage_block", OpConjureMageBlock())
		register(HexPattern.fromAngles("deeqa", HexDir.NORTH_WEST), "modify_block_bouncy", OpModifyMageBlock("bouncy"))
		register(HexPattern.fromAngles("deewwaawd", HexDir.NORTH_WEST), "modify_block_ephemeral", OpModifyMageBlock("ephemeral", 1))
		register(HexPattern.fromAngles("deeqedeaqqqwqqq", HexDir.NORTH_WEST), "modify_block_invisible", OpModifyMageBlock("invisible"))
		register(HexPattern.fromAngles("deewqaqqqqq", HexDir.NORTH_WEST), "modify_block_replaceable", OpModifyMageBlock("replaceable"))
		register(HexPattern.fromAngles("deewedeeeee", HexDir.NORTH_WEST), "modify_block_volatile", OpModifyMageBlock("volatile"))

		register(HexPattern.fromAngles("wwwwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST), "conjure_staff", OpConjureStaff())
		register(HexPattern.fromAngles("aawqqqq", HexDir.SOUTH_EAST), "chorus_blink", OpChorusBlink())

		register(HexPattern.fromAngles("ade", HexDir.SOUTH_WEST), "conjure_speck", OpConjureSpeck())
		register(HexPattern.fromAngles("adeqaa", HexDir.SOUTH_WEST), "move_speck", OpMoveSpeck())
		register(HexPattern.fromAngles("adeaw", HexDir.SOUTH_WEST), "rotate_speck", OpRotateSpeck())
		register(HexPattern.fromAngles("adeeaqa", HexDir.SOUTH_WEST), "iota_speck", OpIotaSpeck())
		register(HexPattern.fromAngles("adeqqaawdd", HexDir.SOUTH_WEST), "lifetime_speck", OpLifetimeSpeck())
		register(HexPattern.fromAngles("adeaqde", HexDir.SOUTH_WEST), "kill_speck", OpKillSpeck())
		register(HexPattern.fromAngles("adeeqed", HexDir.SOUTH_WEST), "size_speck", OpSizeSpeck())
		register(HexPattern.fromAngles("adeeqw", HexDir.SOUTH_WEST), "thickness_speck", OpThicknessSpeck())
		register(HexPattern.fromAngles("qqqqqwdeddwqde", HexDir.SOUTH_EAST), "zone_speck", OpGetEntitiesBy({ entity -> entity is SpeckEntity }, false))

		register(HexPattern.fromAngles("qqwaqda", HexDir.EAST), "is_burning", OpGetEntityData(0))
		register(HexPattern.fromAngles("qqqqwaadq", HexDir.SOUTH_WEST), "is_wet", OpGetEntityData(1))
		register(HexPattern.fromAngles("eewdead", HexDir.WEST), "burning_time", OpGetEntityBurningTime())
		register(HexPattern.fromAngles("eaq", HexDir.WEST), "is_sprinting", OpGetLivingEntityData(0))
		register(HexPattern.fromAngles("aqaew", HexDir.NORTH_WEST), "is_sleeping", OpGetLivingEntityData(1))
		register(HexPattern.fromAngles("qaqqqqqeeeeedq", HexDir.EAST), "block_hardness", OpGetBlockData(0))
		register(HexPattern.fromAngles("qaqqqqqewaaqddqa", HexDir.EAST), "block_blast_resistance", OpGetBlockData(1))
		register(HexPattern.fromAngles("deedqad", HexDir.WEST), "get_weather", OpGetWeather())

		register(HexPattern.fromAngles("qaqqqqded", HexDir.NORTH_EAST), "get_telepathy", OpGetTelepathy())
		register(HexPattern.fromAngles("qaqqqeaqa", HexDir.NORTH_EAST), "send_telepathy", OpSendTelepathy())
		register(HexPattern.fromAngles("qaqqqewaqwa", HexDir.NORTH_EAST), "shout_telepathy", OpShoutTelepathy())
		register(HexPattern.fromAngles("qaqdee", HexDir.NORTH_EAST), "pling", OpSoundPing(SoundEvents.ENTITY_PLAYER_LEVELUP))
		register(HexPattern.fromAngles("qaqdew", HexDir.NORTH_EAST), "click", OpSoundPing(SoundEvents.UI_BUTTON_CLICK))

		register(HexPattern.fromAngles("aqwqaeaqa", HexDir.WEST), "write_grimoire", OpGrimoireWrite())
		register(HexPattern.fromAngles("aqwqaqded", HexDir.WEST), "erase_grimoire", OpGrimoireErase())
		register(HexPattern.fromAngles("aqaeaqwqa", HexDir.SOUTH_EAST), "index_grimoire", OpGrimoireIndex())
		register(HexPattern.fromAngles("dedqdewed", HexDir.SOUTH_WEST), "restrict_grimoire", OpGrimoireRestrict())
		register(HexPattern.fromAngles("aqaedewed", HexDir.NORTH_WEST), "query_grimoire", OpGrimoireQuery())

		register(HexPattern.fromAngles("qaqwawqwqqwqwqwqwqwqq", HexDir.EAST), "make_genie", OpMakeGenie())
		register(HexPattern.fromAngles("eweweweweweewedeaqqqd", HexDir.NORTH_WEST), "educate_genie", OpEducateGenie())
		register(HexPattern.fromAngles("qwddedqdd", HexDir.SOUTH_WEST), "get_lamp_position", OpGetLampData(0))
		register(HexPattern.fromAngles("qwddedadw", HexDir.SOUTH_WEST), "get_lamp_rotation", OpGetLampData(1))
		register(HexPattern.fromAngles("qwddedqew", HexDir.SOUTH_WEST), "get_lamp_velocity", OpGetLampData(2))
		register(HexPattern.fromAngles("qwddedqwddwa", HexDir.SOUTH_WEST), "get_lamp_use_time", OpGetLampData(3))
		register(HexPattern.fromAngles("qwddedaeeeee", HexDir.SOUTH_WEST), "get_lamp_media", OpGetLampData(4))

		register(HexPattern.fromAngles("qaqwddedqdd", HexDir.NORTH_EAST), "get_arch_lamp_position", OpGetArchLampData(0))
		register(HexPattern.fromAngles("qaqwddedadw", HexDir.NORTH_EAST), "get_arch_lamp_rotation", OpGetArchLampData(1))
		register(HexPattern.fromAngles("qaqwddedqew", HexDir.NORTH_EAST), "get_arch_lamp_velocity", OpGetArchLampData(2))
		register(HexPattern.fromAngles("qaqwddedqwddwa", HexDir.NORTH_EAST), "get_arch_lamp_use_time", OpGetArchLampData(3))
		register(HexPattern.fromAngles("qaqwddedqwaqqqqq", HexDir.NORTH_EAST), "get_arch_lamp_storage", OpGetArchLampData(4))
		register(HexPattern.fromAngles("qaqwddedqedeeeee", HexDir.NORTH_EAST), "set_arch_lamp_storage", OpSetArchLampStorage())
		register(HexPattern.fromAngles("qaqwddedaeeeee", HexDir.NORTH_EAST), "get_arch_lamp_media", OpGetArchLampMedia())
		register(HexPattern.fromAngles("qaqwddedwaqdee", HexDir.NORTH_EAST), "terminate_arch_lamp", OpTerminateArchLamp())
		register(HexPattern.fromAngles("qaqwddedqeed", HexDir.NORTH_EAST), "has_arch_lamp", OpIsUsingArchLamp())

		register(HexPattern.fromAngles("aaddaddad", HexDir.EAST), "lamp_finale", OpGetFinale())

		for ((first, second, third) in PATTERNS)
			PatternRegistry.mapPattern(first, second, third)
		for ((first, second, third) in PER_WORLD_PATTERNS)
			PatternRegistry.mapPattern(first, second, third, true)
	}

	private fun register(pattern: HexPattern, name: String, action: Action) = PATTERNS.add(Triple(pattern, Hexical.id(name), action))
	private fun registerPerWorld(pattern: HexPattern, name: String, action: Action) = PER_WORLD_PATTERNS.add(Triple(pattern, Hexical.id(name), action))
}