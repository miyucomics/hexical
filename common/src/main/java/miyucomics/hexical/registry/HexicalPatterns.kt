package miyucomics.hexical.registry

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntitiesBy
import miyucomics.hexical.Hexical
import miyucomics.hexical.casting.operators.OpDupMany
import miyucomics.hexical.casting.operators.OpGetTelepathy
import miyucomics.hexical.casting.operators.OpShufflePattern
import miyucomics.hexical.casting.operators.eval.OpJanus
import miyucomics.hexical.casting.operators.eval.OpNephthys
import miyucomics.hexical.casting.operators.eval.OpSisyphus
import miyucomics.hexical.casting.operators.eval.OpThemis
import miyucomics.hexical.casting.operators.getters.*
import miyucomics.hexical.casting.operators.grimoire.*
import miyucomics.hexical.casting.operators.identifier.OpIdentify
import miyucomics.hexical.casting.operators.identifier.OpRecognize
import miyucomics.hexical.casting.operators.lamp.*
import miyucomics.hexical.casting.operators.specks.*
import miyucomics.hexical.casting.operators.staff.OpReadStaff
import miyucomics.hexical.casting.operators.staff.OpWriteStaff
import miyucomics.hexical.casting.operators.stash.OpStashRead
import miyucomics.hexical.casting.operators.stash.OpStashWrite
import miyucomics.hexical.casting.spells.OpChorusBlink
import miyucomics.hexical.casting.spells.OpConjureHexburst
import miyucomics.hexical.casting.spells.OpConjureHextito
import miyucomics.hexical.casting.spells.OpConjureStaff
import miyucomics.hexical.casting.spells.circle.OpDisplace
import miyucomics.hexical.casting.spells.lamp.OpEducateGenie
import miyucomics.hexical.casting.spells.lamp.OpMakeGenie
import miyucomics.hexical.casting.spells.lamp.OpSetArchLampStorage
import miyucomics.hexical.casting.spells.lamp.OpTerminateArchLamp
import miyucomics.hexical.casting.spells.mage_blocks.OpConjureMageBlock
import miyucomics.hexical.casting.spells.mage_blocks.OpModifyMageBlock
import miyucomics.hexical.casting.spells.telepathy.OpHallucinateSound
import miyucomics.hexical.casting.spells.telepathy.OpSendTelepathy
import miyucomics.hexical.casting.spells.telepathy.OpShoutTelepathy
import miyucomics.hexical.entities.SpeckEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

object HexicalPatterns {
	private val PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private val PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	@JvmStatic
	fun init() {
		register("dup_many", "waadadaa", HexDir.EAST, OpDupMany())
		register("shuffle_pattern", "aqqqdae", HexDir.NORTH_EAST, OpShufflePattern())
		register("chorus_blink", "aawqqqq", HexDir.SOUTH_EAST, OpChorusBlink())
		register("displace", "qaqqqqeedaqqqa", HexDir.NORTH_EAST, OpDisplace())

		register("conjure_mage_block", "dee", HexDir.NORTH_WEST, OpConjureMageBlock())
		register("modify_block_bouncy", "deeqa", HexDir.NORTH_WEST, OpModifyMageBlock("bouncy"))
		register("modify_block_energized", "deewad", HexDir.NORTH_WEST, OpModifyMageBlock("energized", 1))
		register("modify_block_ephemeral", "deewwaawd", HexDir.NORTH_WEST, OpModifyMageBlock("ephemeral", 1))
		register("modify_block_invisible", "deeqedeaqqqwqqq", HexDir.NORTH_WEST, OpModifyMageBlock("invisible"))
		register("modify_block_replaceable", "deewqaqqqqq", HexDir.NORTH_WEST, OpModifyMageBlock("replaceable"))
		register("modify_block_volatile", "deewedeeeee", HexDir.NORTH_WEST, OpModifyMageBlock("volatile"))

		register("conjure_staff", "wwwwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpConjureStaff())
		register("write_staff", "waqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpWriteStaff())
		register("read_staff", "waqqqqqedeqdqdqdqdqe", HexDir.NORTH_EAST, OpReadStaff())
		register("conjure_hexburst", "edeqaawaa", HexDir.SOUTH_WEST, OpConjureHexburst())
		register("conjure_hextito", "edeaddadd", HexDir.SOUTH_WEST, OpConjureHextito())

		register("conjure_speck", "ade", HexDir.SOUTH_WEST, OpConjureSpeck())
		register("move_speck", "adeqaa", HexDir.SOUTH_WEST, OpMoveSpeck())
		register("rotate_speck", "adeaw", HexDir.SOUTH_WEST, OpRotateSpeck())
		register("iota_speck", "adeeaqa", HexDir.SOUTH_WEST, OpIotaSpeck())
		register("lifetime_speck", "adeqqaawdd", HexDir.SOUTH_WEST, OpLifetimeSpeck())
		register("kill_speck", "adeaqde", HexDir.SOUTH_WEST, OpKillSpeck())
		register("size_speck", "adeeqed", HexDir.SOUTH_WEST, OpSizeSpeck())
		register("thickness_speck", "adeeqw", HexDir.SOUTH_WEST, OpThicknessSpeck())
		register("z_rotation_speck", "adeqqqqq", HexDir.SOUTH_WEST, OpZRotationSpeck())
		register("zone_speck", "qqqqqwdeddwqde", HexDir.SOUTH_EAST, OpGetEntitiesBy({ entity -> entity is SpeckEntity }, false))

		register("get_telepathy", "wqqadaw", HexDir.EAST, OpGetTelepathy())
		register("send_telepathy", "qqqqwaqa", HexDir.EAST, OpSendTelepathy())
		register("shout_telepathy", "daqqqqwa", HexDir.EAST, OpShoutTelepathy())
		register("pling", "eqqqada", HexDir.NORTH_EAST, OpHallucinateSound(SoundEvents.ENTITY_PLAYER_LEVELUP))
		register("click", "eqqadaq", HexDir.NORTH_EAST, OpHallucinateSound(SoundEvents.UI_BUTTON_CLICK))

		register("write_grimoire", "aqwqaeaqa", HexDir.WEST, OpGrimoireWrite())
		register("erase_grimoire", "aqwqaqded", HexDir.WEST, OpGrimoireErase())
		register("index_grimoire", "aqaeaqwqa", HexDir.SOUTH_EAST, OpGrimoireIndex())
		register("restrict_grimoire", "dedqdewed", HexDir.SOUTH_WEST, OpGrimoireRestrict())
		register("query_grimoire", "aqaedewed", HexDir.NORTH_WEST, OpGrimoireQuery())

		register("make_genie", "qaqwawqwqqwqwqwqwqwqq", HexDir.EAST, OpMakeGenie())
		register("educate_genie", "eweweweweweewedeaqqqd", HexDir.NORTH_WEST, OpEducateGenie())
		register("get_lamp_position", "qwddedqdd", HexDir.SOUTH_WEST, OpGetLampData(0))
		register("get_lamp_rotation", "qwddedadw", HexDir.SOUTH_WEST, OpGetLampData(1))
		register("get_lamp_velocity", "qwddedqew", HexDir.SOUTH_WEST, OpGetLampData(2))
		register("get_lamp_use_time", "qwddedqwddwa", HexDir.SOUTH_WEST, OpGetLampData(3))
		register("get_lamp_media", "qwddedaeeeee", HexDir.SOUTH_WEST, OpGetLampData(4))
		register("get_arch_lamp_position", "qaqwddedqdd", HexDir.NORTH_EAST, OpGetArchLampData(0))
		register("get_arch_lamp_rotation", "qaqwddedadw", HexDir.NORTH_EAST, OpGetArchLampData(1))
		register("get_arch_lamp_velocity", "qaqwddedqew", HexDir.NORTH_EAST, OpGetArchLampData(2))
		register("get_arch_lamp_use_time", "qaqwddedqwddwa", HexDir.NORTH_EAST, OpGetArchLampData(3))
		register("get_arch_lamp_storage", "qaqwddedqwaqqqqq", HexDir.NORTH_EAST, OpGetArchLampData(4))
		register("set_arch_lamp_storage", "qaqwddedqedeeeee", HexDir.NORTH_EAST, OpSetArchLampStorage())
		register("get_arch_lamp_media", "qaqwddedaeeeee", HexDir.NORTH_EAST, OpGetArchLampMedia())
		register("terminate_arch_lamp", "qaqwddedwaqdee", HexDir.NORTH_EAST, OpTerminateArchLamp())
		register("has_arch_lamp", "qaqwddedqeed", HexDir.NORTH_EAST, OpIsUsingArchLamp())
		register("lamp_finale", "aaddaddad", HexDir.EAST, OpGetFinale())

		register("identify", "qqqqqe", HexDir.NORTH_EAST, OpIdentify())
		register("recognize", "eeeeeq", HexDir.WEST, OpRecognize())
		register("get_mainhand_stack", "qaqqqq", HexDir.NORTH_EAST, OpGetPlayerData(0))
		register("get_offhand_stack", "edeeee", HexDir.NORTH_WEST, OpGetPlayerData(1))
		register("get_weather", "deedqad", HexDir.WEST, OpGetWeather())
		register("get_dimension", "qwqwqwqwqwqqaedwaqd", HexDir.WEST, OpGetDimension())
		register("count_stack", "qaqqwqqqw", HexDir.EAST, OpGetItemStackData(0))
		register("count_max_stack", "edeeweeew", HexDir.WEST, OpGetItemStackData(1))
		register("damage_stack", "eeweeewdeq", HexDir.NORTH_EAST, OpGetItemStackData(2))
		register("damage_max_stack", "qqwqqqwaqe", HexDir.NORTH_WEST, OpGetItemStackData(3))
		register("edible", "adaqqqdd", HexDir.WEST, OpGetItemStackData(4))
		register("get_hunger", "adaqqqddqe", HexDir.WEST, OpGetFoodData(0))
		register("get_saturation", "adaqqqddqw", HexDir.WEST, OpGetFoodData(1))
		register("is_meat", "adaqqqddaed", HexDir.WEST, OpGetFoodData(2))
		register("is_snack", "adaqqqddaq", HexDir.WEST, OpGetFoodData(3))
		register("is_burning", "qqwaqda", HexDir.EAST, OpGetEntityData(0))
		register("burning_time", "eewdead", HexDir.WEST, OpGetEntityData(1))
		register("is_wet", "qqqqwaadq", HexDir.SOUTH_WEST, OpGetEntityData(2))
		register("is_sleeping", "aqaew", HexDir.NORTH_WEST, OpGetLivingEntityData(0))
		register("is_sprinting", "eaq", HexDir.WEST, OpGetLivingEntityData(1))
		register("get_enchantments", "waqeaeqawqwawaw", HexDir.WEST, OpGetItemStackData(5))
		register("get_enchantment_strength", "waqwwqaweede", HexDir.WEST, OpGetEnchantmentStrength())
		register("get_player_hunger", "qqqadaddw", HexDir.WEST, OpGetPlayerData(2))
		register("get_player_saturation", "qqqadaddq", HexDir.WEST, OpGetPlayerData(3))
		register("block_hardness", "qaqqqqqeeeeedq", HexDir.EAST, OpGetBlockData(0))
		register("block_blast_resistance", "qaqqqqqewaaqddqa", HexDir.EAST, OpGetBlockData(1))
		register("get_effects_entity", "wqqq", HexDir.SOUTH_WEST, OpGetLivingEntityData(2))
		register("get_effects_item", "wqqqadee", HexDir.SOUTH_WEST, OpGetPrescription())
		register("get_effect_category", "wqqqaawd", HexDir.SOUTH_WEST, OpGetStatusEffectCategory())
		register("get_effect_amplifier", "wqqqaqwa", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData(0))
		register("get_effect_duration", "wqqqaqwdd", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData(1))

		register("janus", "aadee", HexDir.SOUTH_WEST, OpJanus)
		register("sisyphus", "qaqwede", HexDir.NORTH_EAST, OpSisyphus)
		register("themis", "dwaad", HexDir.WEST, OpThemis)
		PatternRegistry.addSpecialHandler(Hexical.id("nephthys")) { pat ->
			val sig = pat.anglesSignature()
			if (sig.startsWith("deaqqd")) {
				val chars = sig.substring(6)
				var depth = 1
				chars.forEachIndexed { index, char ->
					if (char != "qe"[index % 2])
						return@addSpecialHandler null
					depth += 1
				}
				return@addSpecialHandler OpNephthys(depth)
			}
			return@addSpecialHandler null
		}

		PatternRegistry.addSpecialHandler(Hexical.id("stash_write")) { pat ->
			val sig = pat.anglesSignature()
			if (sig.startsWith("qaawae"))
				return@addSpecialHandler OpStashWrite(HexPattern.fromAngles(pat.anglesSignature().substring(6), pat.startDir))
			return@addSpecialHandler null
		}
		PatternRegistry.addSpecialHandler(Hexical.id("stash_read")) { pat ->
			val sig = pat.anglesSignature()
			if (sig.startsWith("addqae"))
				return@addSpecialHandler OpStashRead(HexPattern.fromAngles(pat.anglesSignature().substring(6), pat.startDir))
			return@addSpecialHandler null
		}

		for ((first, second, third) in PATTERNS)
			PatternRegistry.mapPattern(first, second, third)
		for ((first, second, third) in PER_WORLD_PATTERNS)
			PatternRegistry.mapPattern(first, second, third, true)
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) = PATTERNS.add(Triple(HexPattern.fromAngles(signature, startDir), Hexical.id(name), action))
	private fun registerPerWorld(pattern: HexPattern, name: String, action: Action) = PER_WORLD_PATTERNS.add(Triple(pattern, Hexical.id(name), action))
}