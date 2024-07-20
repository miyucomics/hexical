package miyucomics.hexical.registry

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntitiesBy
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.patterns.*
import miyucomics.hexical.casting.patterns.akashic.OpReadAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpWriteAkashicShelf
import miyucomics.hexical.casting.patterns.circle.OpDisplace
import miyucomics.hexical.casting.patterns.conjure.OpConjureCompass
import miyucomics.hexical.casting.patterns.conjure.OpConjureHexburst
import miyucomics.hexical.casting.patterns.conjure.OpConjureHextito
import miyucomics.hexical.casting.patterns.staff.OpConjureStaff
import miyucomics.hexical.casting.patterns.staff.OpReadStaff
import miyucomics.hexical.casting.patterns.staff.OpWriteStaff
import miyucomics.hexical.casting.patterns.dye.OpDye
import miyucomics.hexical.casting.patterns.dye.OpGetDye
import miyucomics.hexical.casting.patterns.dye.OpMimicDye
import miyucomics.hexical.casting.patterns.eval.OpJanus
import miyucomics.hexical.casting.patterns.eval.OpNephthys
import miyucomics.hexical.casting.patterns.eval.OpSisyphus
import miyucomics.hexical.casting.patterns.eval.OpThemis
import miyucomics.hexical.casting.patterns.fireworks.OpConjureFirework
import miyucomics.hexical.casting.patterns.fireworks.OpSimulateFirework
import miyucomics.hexical.casting.patterns.getters.*
import miyucomics.hexical.casting.patterns.getters.misc.OpGetEnchantmentStrength
import miyucomics.hexical.casting.patterns.getters.misc.OpGetStatusEffectCategory
import miyucomics.hexical.casting.patterns.getters.types.OpGetBlockTypeData
import miyucomics.hexical.casting.patterns.getters.types.OpGetFoodTypeData
import miyucomics.hexical.casting.patterns.getters.types.OpGetItemTypeData
import miyucomics.hexical.casting.patterns.grimoire.*
import miyucomics.hexical.casting.patterns.identifier.OpIdentify
import miyucomics.hexical.casting.patterns.identifier.OpRecognize
import miyucomics.hexical.casting.patterns.lamp.*
import miyucomics.hexical.casting.patterns.mage_blocks.OpConjureMageBlock
import miyucomics.hexical.casting.patterns.mage_blocks.OpModifyMageBlock
import miyucomics.hexical.casting.patterns.prestidigitation.OpCanPrestidigitation
import miyucomics.hexical.casting.patterns.prestidigitation.OpPrestidigitation
import miyucomics.hexical.casting.patterns.soroban.OpSorobanDecrement
import miyucomics.hexical.casting.patterns.soroban.OpSorobanIncrement
import miyucomics.hexical.casting.patterns.soroban.OpSorobanReset
import miyucomics.hexical.casting.patterns.specks.*
import miyucomics.hexical.casting.patterns.telepathy.OpHallucinateSound
import miyucomics.hexical.casting.patterns.telepathy.OpSendTelepathy
import miyucomics.hexical.casting.patterns.telepathy.OpShoutTelepathy
import miyucomics.hexical.casting.patterns.wristpocket.OpGetWristpocket
import miyucomics.hexical.casting.patterns.wristpocket.OpIngest
import miyucomics.hexical.casting.patterns.wristpocket.OpMageHand
import miyucomics.hexical.casting.patterns.wristpocket.OpWristpocket
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.Specklike
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

object HexicalPatterns {
	private val PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()
	private val PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, Identifier, Action>> = ArrayList()

	@JvmStatic
	fun init() {
		registerPerWorld("greater_blink", "wqawawaqwqwqawawaqw", HexDir.SOUTH_WEST, OpGreaterBlink())

		register("conjure_mesh", "wwaqaa", HexDir.EAST, OpConjureMesh())
		register("weave_mesh", "wwaqaaw", HexDir.EAST, OpWeaveMesh())

		register("mimic_dye", "awddwqaeqqqeaeqqq", HexDir.EAST, OpMimicDye())

		register("perlin", "qawedqdq", HexDir.WEST, OpPerlin())
		register("theodolite", "wqaa", HexDir.EAST, OpGetEntityData(3))

		register("clear_vision", "qaqwdwqaq", HexDir.EAST, OpShader(null))
		register("owl_vision", "qqqqqedeqqqqq", HexDir.EAST, OpShader(HexicalMain.id("shaders/post/night_vision.json")))
		register("tele_vision", "qaqwdeqwawqwa", HexDir.EAST, OpShader(HexicalMain.id("shaders/post/television.json")))
		register("spider_vision", "aqqqaqwdeqaqded", HexDir.NORTH_WEST, OpShader(Identifier("shaders/post/spider.json")))
		register("phosphor", "qaqwdwdaqqqa", HexDir.EAST, OpShader(Identifier("shaders/post/phosphor.json")))

		register("spike", "qdqdqdqdww", HexDir.NORTH_EAST, OpSpike())

		register("with_hand_lamp", "qwddedqqaqqqqq", HexDir.SOUTH_WEST, OpCheckSource(SpecializedSource.HAND_LAMP))
		register("with_arch_lamp", "qaqwddedqqaqqqqq", HexDir.NORTH_EAST, OpCheckSource(SpecializedSource.ARCH_LAMP))
		register("with_conjured_staff", "waqaeaqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpCheckSource(SpecializedSource.CONJURED_STAFF))
		register("with_evocation", "waeqqqqedeqdqdqdqewee", HexDir.EAST, OpCheckSource(SpecializedSource.EVOCATION))

		register("read_shelf", "qaqqqada", HexDir.EAST, OpReadAkashicShelf())
		register("write_shelf", "edeeedad", HexDir.SOUTH_WEST, OpWriteAkashicShelf())

		register("age_scroll", "wewaawewaddwwewwdwwew", HexDir.SOUTH_EAST, OpAgeScroll())

		register("conjure_firework", "dedwaqwwawwqa", HexDir.SOUTH_WEST, OpConjureFirework())
		register("simulate_firework", "dedwaqwqqwqa", HexDir.SOUTH_WEST, OpSimulateFirework())

		register("get_dye", "weedwa", HexDir.NORTH_EAST, OpGetDye())
		register("dye", "dwaqqw", HexDir.NORTH_WEST, OpDye())

		register("soroban_decrement", "waqdee", HexDir.SOUTH_EAST, OpSorobanDecrement())
		register("soroban_increment", "wdeaqq", HexDir.NORTH_EAST, OpSorobanIncrement())
		register("soroban_reset", "qdeeaae", HexDir.NORTH_EAST, OpSorobanReset())

		register("prestidigitation", "wedewedew", HexDir.NORTH_EAST, OpPrestidigitation())
		register("can_prestidigitate", "wqaqwqaqw", HexDir.NORTH_WEST, OpCanPrestidigitation())

		register("magic_missile", "qaqww", HexDir.WEST, OpMagicMissile())

		register("internalize_hex", "waeqqqqedeqdqdqdqeqdwwd", HexDir.EAST, OpInternalizeHex())

		register("wristpocket", "aaqqa", HexDir.WEST, OpWristpocket())
		register("wristpocket_item", "aaqqada", HexDir.WEST, OpGetWristpocket(0))
		register("wristpocket_count", "aaqqaaw", HexDir.WEST, OpGetWristpocket(1))
		register("mage_hand", "aaqqaeea", HexDir.WEST, OpMageHand())
		register("ingest", "aaqqadaa", HexDir.WEST, OpIngest())

		register("similar", "dew", HexDir.NORTH_WEST, OpSimilar())
		register("congruent", "aaqd", HexDir.EAST, OpCongruentPattern())
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
		register("modify_block_semipermeable", "deeeqawde", HexDir.NORTH_WEST, OpModifyMageBlock("semipermeable"))
		register("modify_block_volatile", "deewedeeeee", HexDir.NORTH_WEST, OpModifyMageBlock("volatile"))

		register("conjure_staff", "wwwwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpConjureStaff())
		register("write_staff", "waqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpWriteStaff())
		register("read_staff", "waqqqqqedeqdqdqdqdqe", HexDir.NORTH_EAST, OpReadStaff())

		register("conjure_compass", "aqwawqwqqwqwqeawwa", HexDir.SOUTH_WEST, OpConjureCompass())
		register("conjure_hexburst", "edeqaawaa", HexDir.SOUTH_WEST, OpConjureHexburst())
		register("conjure_hextito", "edeaddadd", HexDir.SOUTH_WEST, OpConjureHextito())

		register("conjure_speck", "ade", HexDir.SOUTH_WEST, OpConjureSpeck())
		register("iota_speck", "adeeaqa", HexDir.SOUTH_WEST, OpIotaSpeck())
		register("kill_specklike", "adeaqde", HexDir.SOUTH_WEST, OpKillSpecklike())
		register("move_specklike", "adeqaa", HexDir.SOUTH_WEST, OpSpecklikeProperty(0))
		register("rotate_specklike", "adeaw", HexDir.SOUTH_WEST, OpSpecklikeProperty(1))
		register("roll_specklike", "adeqqqqq", HexDir.SOUTH_WEST, OpSpecklikeProperty(2))
		register("size_specklike", "adeeqed", HexDir.SOUTH_WEST, OpSpecklikeProperty(3))
		register("thickness_specklike", "adeeqw", HexDir.SOUTH_WEST, OpSpecklikeProperty(4))
		register("lifetime_specklike", "adeqqaawdd", HexDir.SOUTH_WEST, OpSpecklikeProperty(5))
		register("zone_specklike", "qqqqqwdeddwqde", HexDir.SOUTH_EAST, OpGetEntitiesBy({ entity -> entity is Specklike }, false))

		register("get_telepathy", "wqqadaw", HexDir.EAST, OpGetKeybind("key.hexical.telepathy"))
		register("send_telepathy", "qqqqwaqa", HexDir.EAST, OpSendTelepathy())
		register("shout_telepathy", "daqqqqwa", HexDir.EAST, OpShoutTelepathy())
		register("pling", "eqqqada", HexDir.NORTH_EAST, OpHallucinateSound(SoundEvents.ENTITY_PLAYER_LEVELUP))
		register("click", "eqqadaq", HexDir.NORTH_EAST, OpHallucinateSound(SoundEvents.UI_BUTTON_CLICK))
		register("moving_left", "edead", HexDir.SOUTH_EAST, OpGetKeybind("key.left"))
		register("moving_right", "qaqda", HexDir.SOUTH_WEST, OpGetKeybind("key.right"))
		register("moving_up", "aqaddq", HexDir.SOUTH_EAST, OpGetKeybind("key.forward"))
		register("moving_down", "dedwdq", HexDir.SOUTH_WEST, OpGetKeybind("key.back"))

		register("write_grimoire", "aqwqaeaqa", HexDir.WEST, OpGrimoireWrite())
		register("erase_grimoire", "aqwqaqded", HexDir.WEST, OpGrimoireErase())
		register("index_grimoire", "aqaeaqwqa", HexDir.SOUTH_EAST, OpGrimoireIndex())
		register("restrict_grimoire", "dedqdewed", HexDir.SOUTH_WEST, OpGrimoireRestrict())
		register("query_grimoire", "aqaedewed", HexDir.NORTH_WEST, OpGrimoireQuery())

		register("offer_mind", "qaqwawqwqqwqwqwqwqwqq", HexDir.EAST, OpReloadLamp())
		register("educate_genie", "eweweweweweewedeaqqqd", HexDir.NORTH_WEST, OpEducateGenie())
		register("get_hand_lamp_position", "qwddedqdd", HexDir.SOUTH_WEST, OpGetHandLampData(0))
		register("get_hand_lamp_rotation", "qwddedadw", HexDir.SOUTH_WEST, OpGetHandLampData(1))
		register("get_hand_lamp_velocity", "qwddedqew", HexDir.SOUTH_WEST, OpGetHandLampData(2))
		register("get_hand_lamp_use_time", "qwddedqwddwa", HexDir.SOUTH_WEST, OpGetHandLampData(3))
		register("get_hand_lamp_media", "qwddedaeeeee", HexDir.SOUTH_WEST, OpGetHandLampData(4))
		register("get_hand_lamp_storage", "qwddedqwaqqqqq", HexDir.SOUTH_WEST, OpGetHandLampData(5))
		register("set_hand_lamp_storage", "qwddedqedeeeee", HexDir.SOUTH_WEST, OpSetHandLampStorage())
		register("get_arch_lamp_position", "qaqwddedqdd", HexDir.NORTH_EAST, OpGetArchLampData(0))
		register("get_arch_lamp_rotation", "qaqwddedadw", HexDir.NORTH_EAST, OpGetArchLampData(1))
		register("get_arch_lamp_velocity", "qaqwddedqew", HexDir.NORTH_EAST, OpGetArchLampData(2))
		register("get_arch_lamp_use_time", "qaqwddedqwddwa", HexDir.NORTH_EAST, OpGetArchLampData(3))
		register("get_arch_lamp_storage", "qaqwddedqwaqqqqq", HexDir.NORTH_EAST, OpGetArchLampData(4))
		register("set_arch_lamp_storage", "qaqwddedqedeeeee", HexDir.NORTH_EAST, OpSetArchLampStorage())
		register("get_arch_lamp_media", "qaqwddedaeeeee", HexDir.NORTH_EAST, OpGetArchLampMedia())
		register("activate_arch_lamp", "qaqwddedadeaqq", HexDir.NORTH_EAST, OpActivateArchLamp())
		register("terminate_arch_lamp", "qaqwddedwaqdee", HexDir.NORTH_EAST, OpTerminateArchLamp())
		register("has_arch_lamp", "qaqwddedqeed", HexDir.NORTH_EAST, OpIsUsingArchLamp())
		register("lamp_finale", "aaddaddad", HexDir.EAST, OpGetFinale())

		register("identify", "qqqqqe", HexDir.NORTH_EAST, OpIdentify())
		register("recognize", "eeeeeq", HexDir.WEST, OpRecognize())
		register("get_mainhand_stack", "qaqqqq", HexDir.NORTH_EAST, OpGetPlayerData(0))
		register("get_offhand_stack", "edeeee", HexDir.NORTH_WEST, OpGetPlayerData(1))
		register("get_weather", "eweweweweweeeaedqdqde", HexDir.WEST, OpGetWorldData(0))
		register("get_dimension", "qwqwqwqwqwqqaedwaqd", HexDir.WEST, OpGetWorldData(1))
		register("get_time", "wddwaqqwqaddaqqwddwaqqwqaddaq", HexDir.SOUTH_EAST, OpGetWorldData(2))
		register("get_light", "wqwqwqwqwqwaeqqqqaeqaeaeaeaw", HexDir.SOUTH_WEST, OpGetPositionData(0))
		register("get_biome", "qwqwqawdqqaqqdwaqwqwq", HexDir.WEST, OpGetPositionData(1))
		register("count_stack", "qaqqwqqqw", HexDir.EAST, OpGetItemStackData(0))
		register("damage_stack", "eeweeewdeq", HexDir.NORTH_EAST, OpGetItemStackData(1))

		register("count_max_stack", "edeeweeew", HexDir.WEST, OpGetItemTypeData(0))
		register("damage_max_stack", "qqwqqqwaqe", HexDir.NORTH_WEST, OpGetItemTypeData(1))
		register("edible", "adaqqqdd", HexDir.WEST, OpGetItemTypeData(2))

		register("get_hunger", "adaqqqddqe", HexDir.WEST, OpGetFoodTypeData(0))
		register("get_saturation", "adaqqqddqw", HexDir.WEST, OpGetFoodTypeData(1))
		register("is_meat", "adaqqqddaed", HexDir.WEST, OpGetFoodTypeData(2))
		register("is_snack", "adaqqqddaq", HexDir.WEST, OpGetFoodTypeData(3))
		register("is_burning", "qqwaqda", HexDir.EAST, OpGetEntityData(0))
		register("burning_time", "eewdead", HexDir.WEST, OpGetEntityData(1))
		register("is_wet", "qqqqwaadq", HexDir.SOUTH_WEST, OpGetEntityData(2))
		register("get_health", "wddwaqqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData(0))
		register("get_max_health", "wddwwawaeqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData(1))
		register("get_air", "wwaade", HexDir.EAST, OpGetLivingEntityData(2))
		register("get_max_air", "wwaadee", HexDir.EAST, OpGetLivingEntityData(3))
		register("is_sleeping", "aqaew", HexDir.NORTH_WEST, OpGetLivingEntityData(4))
		register("is_sprinting", "eaq", HexDir.WEST, OpGetLivingEntityData(5))

		register("get_enchantments", "waqeaeqawqwawaw", HexDir.WEST, OpGetItemStackData(2))
		register("get_enchantment_strength", "waqwwqaweede", HexDir.WEST, OpGetEnchantmentStrength())

		register("get_player_hunger", "qqqadaddw", HexDir.WEST, OpGetPlayerData(2))
		register("get_player_saturation", "qqqadaddq", HexDir.WEST, OpGetPlayerData(3))

		register("block_hardness", "qaqqqqqeeeeedq", HexDir.EAST, OpGetBlockTypeData(0))
		register("block_blast_resistance", "qaqqqqqewaaqddqa", HexDir.EAST, OpGetBlockTypeData(1))
		register("blockstate_waterlogged", "edeeeeeqwqqqqw", HexDir.SOUTH_EAST, OpGetBlockStateData(0))
		register("blockstate_rotation", "qaqqqqqwadeeed", HexDir.EAST, OpGetBlockStateData(1))
		register("blockstate_crop", "qaqqqqqwaea", HexDir.EAST, OpGetBlockStateData(2))
		register("blockstate_glow", "qaqqqqqwaeaeaeaeaea", HexDir.EAST, OpGetBlockStateData(3))
		register("blockstate_lock", "qaqqqeaqwdewd", HexDir.EAST, OpGetBlockStateData(4))
		register("blockstate_turn", "qaqqqqqwqqwqd", HexDir.EAST, OpGetBlockStateData(5))
		register("blockstate_bunch", "qaqqqqqweeeeedeeqaqdeee", HexDir.EAST, OpGetBlockStateData(6))
		register("blockstate_book", "qaqqqqqeawa", HexDir.EAST, OpGetBlockStateData(7))

		register("get_effects_entity", "wqqq", HexDir.SOUTH_WEST, OpGetLivingEntityData(6))
		register("get_effects_item", "wqqqadee", HexDir.SOUTH_WEST, OpGetPrescription())
		register("get_effect_category", "wqqqaawd", HexDir.SOUTH_WEST, OpGetStatusEffectCategory())
		register("get_effect_amplifier", "wqqqaqwa", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData(0))
		register("get_effect_duration", "wqqqaqwdd", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData(1))

		register("janus", "aadee", HexDir.SOUTH_WEST, OpJanus)
		register("sisyphus", "qaqwede", HexDir.NORTH_EAST, OpSisyphus)
		register("themis", "dwaad", HexDir.WEST, OpThemis)
		PatternRegistry.addSpecialHandler(HexicalMain.id("nephthys")) { pat ->
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

		for ((first, second, third) in PATTERNS)
			PatternRegistry.mapPattern(first, second, third)
		for ((first, second, third) in PER_WORLD_PATTERNS)
			PatternRegistry.mapPattern(first, second, third, true)
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) = PATTERNS.add(Triple(HexPattern.fromAngles(signature, startDir), HexicalMain.id(name), action))
	private fun registerPerWorld(name: String, signature: String, startDir: HexDir, action: Action) = PER_WORLD_PATTERNS.add(Triple(HexPattern.fromAngles(signature, startDir), HexicalMain.id(name), action))
}