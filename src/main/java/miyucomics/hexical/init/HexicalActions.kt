package miyucomics.hexical.init

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.getLong
import at.petrak.hexcasting.api.utils.vecFromNBT
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.petrak.hexcasting.common.lib.hex.HexArithmetics
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.feature.akashic_shelf.OpClearAkashicShelf
import miyucomics.hexical.feature.akashic_shelf.OpKeyAkashicShelf
import miyucomics.hexical.feature.akashic_shelf.OpReadAkashicShelf
import miyucomics.hexical.feature.akashic_shelf.OpWriteAkashicShelf
import miyucomics.hexical.feature.autograph.OpAutograph
import miyucomics.hexical.feature.autograph.OpHasAutograph
import miyucomics.hexical.feature.autograph.OpUnautograph
import miyucomics.hexical.feature.block_mimicry.OpCook
import miyucomics.hexical.feature.block_mimicry.OpDispense
import miyucomics.hexical.feature.block_mimicry.OpPiston
import miyucomics.hexical.feature.block_mimicry.OpStonecut
import miyucomics.hexical.feature.charming.OpCharmItem
import miyucomics.hexical.feature.charming.OpDischarmItem
import miyucomics.hexical.feature.charming.OpReadCharmedProxy
import miyucomics.hexical.feature.charming.OpWriteCharmedProxy
import miyucomics.hexical.feature.confection.OpConjureGummy
import miyucomics.hexical.feature.confection.OpConjureHexburst
import miyucomics.hexical.feature.confection.OpConjureHextito
import miyucomics.hexical.feature.confetti.OpConfetti
import miyucomics.hexical.feature.conjure.OpConjureEntity
import miyucomics.hexical.feature.conjure.OpConjureLight
import miyucomics.hexical.feature.dda.OpDda
import miyucomics.hexical.feature.driver_dot.OpProgramDriver
import miyucomics.hexical.feature.dyeing.actions.OpDye
import miyucomics.hexical.feature.dyeing.actions.OpGetDye
import miyucomics.hexical.feature.dyeing.actions.OpTranslateDye
import miyucomics.hexical.feature.evocation.OpGetEvocation
import miyucomics.hexical.feature.evocation.OpSetEvocation
import miyucomics.hexical.feature.flora.OpConjureFlora
import miyucomics.hexical.feature.genie_lamp.OpGetFinale
import miyucomics.hexical.feature.genie_lamp.OpGetLampData
import miyucomics.hexical.feature.genie_lamp.OpSetLampStorage
import miyucomics.hexical.feature.genie_lamp.OpWish
import miyucomics.hexical.feature.grimoire.OpGrimoireErase
import miyucomics.hexical.feature.grimoire.OpGrimoireIndex
import miyucomics.hexical.feature.grimoire.OpGrimoireWrite
import miyucomics.hexical.feature.grok.OpGrokGetParenthesized
import miyucomics.hexical.feature.grok.OpGrokGetStack
import miyucomics.hexical.feature.grok.OpGrokSetParenthesized
import miyucomics.hexical.feature.grok.OpGrokSetStack
import miyucomics.hexical.feature.itemmind.OpReadItemmind
import miyucomics.hexical.feature.itemmind.OpWriteItemmind
import miyucomics.hexical.feature.jailbreak.OpJailbreakDevice
import miyucomics.hexical.feature.lesser_sentinel.OpLesserSentinelGet
import miyucomics.hexical.feature.lesser_sentinel.OpLesserSentinelSet
import miyucomics.hexical.feature.mage_hand.OpMageHand
import miyucomics.hexical.feature.mage_mouth.OpMageMouth
import miyucomics.hexical.feature.magic_missile.OpMagicMissile
import miyucomics.hexical.feature.misc.*
import miyucomics.hexical.feature.mute.OpIsMute
import miyucomics.hexical.feature.mute.OpMute
import miyucomics.hexical.feature.pattern_manipulation.*
import miyucomics.hexical.feature.periwinkle.OpCompelSniffer
import miyucomics.hexical.feature.personal_inventory.OpAbsorbItem
import miyucomics.hexical.feature.personal_inventory.OpExpelItem
import miyucomics.hexical.feature.personal_inventory.OpListPossessions
import miyucomics.hexical.feature.personal_inventory.OpMoveItem
import miyucomics.hexical.feature.pigment.OpSamplePigment
import miyucomics.hexical.feature.pigment.OpTakeOnPigment
import miyucomics.hexical.feature.pigment.OpToPigment
import miyucomics.hexical.feature.prestidigitation.OpPrestidigitation
import miyucomics.hexical.feature.redstone_pin.OpPinRedstone
import miyucomics.hexical.feature.rotate.OpRotateBlock
import miyucomics.hexical.feature.rotate.OpRotateEntity
import miyucomics.hexical.feature.sentinel_defense.OpExorciseSentinel
import miyucomics.hexical.feature.sentinel_defense.OpHuntSentinel
import miyucomics.hexical.feature.shader.OpShader
import miyucomics.hexical.feature.sparkle.OpSparkle
import miyucomics.hexical.feature.specialized_breaking.OpBreakFortune
import miyucomics.hexical.feature.specialized_breaking.OpBreakSilk
import miyucomics.hexical.feature.specklike.actions.*
import miyucomics.hexical.feature.specklike.mesh.OpConjureMesh
import miyucomics.hexical.feature.specklike.speck.OpConjureSpeck
import miyucomics.hexical.feature.specklike.strand.OpConjureStrand
import miyucomics.hexical.feature.specklike.strand.OpDrawPattern
import miyucomics.hexical.feature.spell_circle.OpAbsorbArm
import miyucomics.hexical.feature.spell_circle.OpDisplace
import miyucomics.hexical.feature.spike.OpConjureSpike
import miyucomics.hexical.feature.telepathy.OpGetKeybindOther
import miyucomics.hexical.feature.telepathy.OpGetKeybindSelf
import miyucomics.hexical.feature.telepathy.OpGetScroll
import miyucomics.hexical.feature.toasting.OpSendTelepathy
import miyucomics.hexical.feature.toasting.OpShoutTelepathy
import miyucomics.hexical.feature.toasting.OpToast
import miyucomics.hexical.feature.wristpocket.OpGetWristpocket
import miyucomics.hexical.feature.wristpocket.OpSleight
import miyucomics.hexical.feature.wristpocket.OpWristpocket
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.FireballEntity
import net.minecraft.entity.projectile.LlamaSpitEntity
import net.minecraft.entity.projectile.thrown.EggEntity
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registry

object HexicalActions {
	fun init() {
		Registry.register(HexArithmetics.REGISTRY, HexicalMain.id("patterns"), PatternArithmetic)

		register("program_driver", "aqqqqaw", HexDir.NORTH_WEST, OpProgramDriver)

		register("write_grimoire", "aqwqaeaqa", HexDir.WEST, OpGrimoireWrite)
		register("erase_grimoire", "aqwqaqded", HexDir.WEST, OpGrimoireErase)
		register("index_grimoire", "aqaeaqwqa", HexDir.SOUTH_EAST, OpGrimoireIndex)

		register("periwinkle", "wwwaqdadaadadqqqeaeq", HexDir.EAST, OpCompelSniffer)

		register("wish", "eweweweweweewedeaqqqd", HexDir.NORTH_WEST, OpWish)
		register("get_lamp_position", "qaqwddedqdd", HexDir.NORTH_EAST, OpGetLampData { _, nbt -> vecFromNBT(nbt.getCompound("position")).asActionResult })
		register("get_lamp_rotation", "qaqwddedadw", HexDir.NORTH_EAST, OpGetLampData { _, nbt -> vecFromNBT(nbt.getCompound("rotation")).asActionResult })
		register("get_lamp_velocity", "qaqwddedqew", HexDir.NORTH_EAST, OpGetLampData { _, nbt -> vecFromNBT(nbt.getCompound("velocity")).asActionResult })
		register("get_lamp_use_time", "qaqwddedqwddwa", HexDir.NORTH_EAST, OpGetLampData { env, nbt -> (env.world.time - (nbt.getDouble("start_time") + 1.0)).asActionResult })
		register("get_lamp_media", "qaqwddedaeeeee", HexDir.NORTH_EAST, OpGetLampData { env, _ -> (env.stack.getLong("media") / MediaConstants.DUST_UNIT).asActionResult })
		register("get_lamp_storage", "qaqwddedqwaqqqqq", HexDir.NORTH_EAST, OpGetLampData { env, nbt -> listOf(IotaType.deserialize(nbt.getCompound("storage"), env.world)) })
		register("set_lamp_storage", "qaqwddedqedeeeee", HexDir.NORTH_EAST, OpSetLampStorage)
		register("lamp_finale", "aaddaddad", HexDir.EAST, OpGetFinale)

		register("rotate_block", "edeeeeeweewadeeed", HexDir.EAST, OpRotateBlock)
		register("rotate_entity", "qqqdaqqqa", HexDir.EAST, OpRotateEntity)

		register("shuffle_pattern", "aqqqdae", HexDir.NORTH_EAST, OpShufflePattern)
		register("congruent", "aaqd", HexDir.EAST, OpCongruentPattern)
		register("similar", "aedd", HexDir.EAST, OpSimilarPattern)
		register("serialize_pattern", "wqaedeqd", HexDir.EAST, OpDisintegratePattern)
		register("deserialize_pattern", "wqqqaqwd", HexDir.EAST, OpIntegratePattern)

		register("grok_get_stack", "aqawwqaw", HexDir.EAST, OpGrokGetStack)
		register("grok_set_stack", "ewdewwde", HexDir.EAST, OpGrokSetStack)
		register("grok_get_parenthesized", "waqwawwqwaw", HexDir.EAST, OpGrokGetParenthesized)
		register("grok_set_parenthesized", "wewdwewwdwe", HexDir.EAST, OpGrokSetParenthesized)

		register("read_itemmind", "aadaqada", HexDir.EAST, OpReadItemmind)
		register("write_itemmind", "ddadedad", HexDir.EAST, OpWriteItemmind)

		register("dda", "wdqqdwewdqqdwdqdadedaddww", HexDir.NORTH_EAST, OpDda)

		register("get_telepathy", "wqqadaw", HexDir.EAST, OpGetKeybindSelf("key.hexical.telepathy"))
		register("left_click", "qadee", HexDir.NORTH_EAST, OpGetKeybindSelf("key.attack"))
		register("right_click", "edaqq", HexDir.NORTH_WEST, OpGetKeybindSelf("key.use"))
		register("moving_up", "aqaddq", HexDir.SOUTH_EAST, OpGetKeybindSelf("key.forward"))
		register("moving_down", "dedwdq", HexDir.SOUTH_WEST, OpGetKeybindSelf("key.back"))
		register("moving_left", "edead", HexDir.SOUTH_EAST, OpGetKeybindSelf("key.left"))
		register("moving_right", "qaqda", HexDir.SOUTH_WEST, OpGetKeybindSelf("key.right"))
		register("sneaking", "wede", HexDir.NORTH_WEST, OpGetKeybindSelf("key.sneak"))
		register("sprinting", "wqaq", HexDir.NORTH_WEST, OpGetKeybindSelf("key.sprint"))
		register("jumping", "qaqdaqqa", HexDir.SOUTH_WEST, OpGetKeybindSelf("key.jump"))
		register("scroll", "qadeeee", HexDir.NORTH_EAST, OpGetScroll)
		register("left_click_other", "wqadee", HexDir.NORTH_EAST, OpGetKeybindOther("key.attack"))
		register("right_click_other", "wedaqq", HexDir.NORTH_WEST, OpGetKeybindOther("key.use"))
		register("moving_up_other", "aqaddqw", HexDir.SOUTH_EAST, OpGetKeybindOther("key.forward"))
		register("moving_down_other", "dedwdqw", HexDir.SOUTH_WEST, OpGetKeybindOther("key.back"))
		register("moving_left_other", "edeadw", HexDir.SOUTH_EAST, OpGetKeybindOther("key.left"))
		register("moving_right_other", "qaqdaw", HexDir.SOUTH_WEST, OpGetKeybindOther("key.right"))
		register("jumping_other", "qaqdawawa", HexDir.SOUTH_WEST, OpGetKeybindOther("key.jump"))
		register("sneaking_other", "wwede", HexDir.NORTH_WEST, OpGetKeybindOther("key.sneak"))

		register("toast", "ewqqqwqqaee", HexDir.NORTH_EAST, OpToast)
		register("send_telepathy", "qqqqwaqa", HexDir.EAST, OpSendTelepathy)
		register("shout_telepathy", "daqqqqwa", HexDir.EAST, OpShoutTelepathy)

		register("key_shelf", "qaqqadaq", HexDir.EAST, OpKeyAkashicShelf)
		register("read_shelf", "qaqqqada", HexDir.EAST, OpReadAkashicShelf)
		register("write_shelf", "edeeedad", HexDir.SOUTH_WEST, OpWriteAkashicShelf)
		register("clear_shelf", "edeedade", HexDir.SOUTH_WEST, OpClearAkashicShelf)

		register("autograph", "eeeeeww", HexDir.WEST, OpAutograph)
		register("unautograph", "wwqqqqq", HexDir.NORTH_EAST, OpUnautograph)
		register("has_autograph", "wwqqqqqaw", HexDir.NORTH_EAST, OpHasAutograph)

		register("get_dye", "weedwa", HexDir.NORTH_EAST, OpGetDye)
		register("dye", "dwaqqw", HexDir.NORTH_WEST, OpDye)
		register("translate_dye", "wdwwaawwewdwwewwdwwe", HexDir.EAST, OpTranslateDye)

		register("magic_missile", "qaqww", HexDir.WEST, OpMagicMissile)

		register("to_pigment", "aqwedeweeeewweeew", HexDir.NORTH_WEST, OpToPigment)
		register("sample_pigment", "edewqaqqqqqwqqq", HexDir.SOUTH_EAST, OpSamplePigment)
		register("take_on_pigment", "weeeweeqeeeewqaqweeee", HexDir.EAST, OpTakeOnPigment)

		register("mage_hand", "dqq", HexDir.NORTH_WEST, OpMageHand)

		register("mage_mouth", "eeede", HexDir.EAST, OpMageMouth)

		register("prestidigitation", "wedewedew", HexDir.NORTH_EAST, OpPrestidigitation)

		register("wristpocket", "aaqqa", HexDir.WEST, OpWristpocket)
		register("wristpocket_item", "aaqqada", HexDir.WEST, OpGetWristpocket)
		register("sleight", "aaqqaded", HexDir.WEST, OpSleight)

		register("conjure_flora", "weqqqqqwaeaeaeaeaea", HexDir.NORTH_EAST, OpConjureFlora)

		register("confetti", "awddeqaedd", HexDir.EAST, OpConfetti)
		register("vibration", "wwawawwd", HexDir.EAST, OpVibrate)
		register("sparkle", "dqa", HexDir.NORTH_EAST, OpSparkle)
		register("jailbreak", "wwaqqqqqeqdedwqeaeqwdedwqeaeq", HexDir.EAST, OpJailbreakDevice)
		register("light", "aeaeaeaeaeawqqqqq", HexDir.SOUTH_EAST, OpConjureLight)
		register("gasp", "aweeeeewaweeeee", HexDir.NORTH_WEST, OpGasp)
		register("parrot", "wweedadw", HexDir.NORTH_EAST, OpImitateParrot)

		register("egg", "qqqwaqaaqeeewdedde", HexDir.SOUTH_EAST, OpConjureEntity(MediaConstants.DUST_UNIT * 2) { world, position, caster -> EggEntity(world, position.x, position.y, position.z).apply { owner = caster } })
		register("llama_spit", "dwqaqw", HexDir.EAST, OpConjureEntity(MediaConstants.DUST_UNIT / 4) { world, position, caster -> LlamaSpitEntity(EntityType.LLAMA_SPIT, world).apply {
			setPosition(position)
			owner = caster
		}})
		register("snowball", "ddeeeeewd", HexDir.NORTH_EAST, OpConjureEntity(MediaConstants.DUST_UNIT / 2) { world, position, caster -> SnowballEntity(world, position.x, position.y, position.z).apply {
			owner = caster
		}})
		register("ghast_fireball", "wqqqqqwaeaeaeaeae", HexDir.SOUTH_EAST, OpConjureEntity(MediaConstants.DUST_UNIT * 3) { world, position, caster -> FireballEntity(world, caster, 0.0, 0.0, 0.0, 1).apply {
			setPosition(position)
		}})

		register("mute", "wddaq", HexDir.EAST, OpMute)
		register("is_mute", "edaaw", HexDir.NORTH_WEST, OpIsMute)

		register("redstone_pin", "qad", HexDir.WEST, OpPinRedstone)

		register("break_fortune", "qaqqqqqdeeeqeee", HexDir.EAST, OpBreakFortune)
		register("break_silk", "aqaeaqdeeweweedq", HexDir.EAST, OpBreakSilk)

		register("conjure_gummy", "eeewdw", HexDir.SOUTH_WEST, OpConjureGummy)
		register("conjure_hexburst", "aadaadqaq", HexDir.EAST, OpConjureHexburst)
		register("conjure_hextito", "qaqdqaqdwawaw", HexDir.EAST, OpConjureHextito)

		register("spike", "qdqdqdqdww", HexDir.NORTH_EAST, OpConjureSpike)

		register("piston", "wqwawqwqqqeqq", HexDir.SOUTH_WEST, OpPiston)
		register("dispense", "wqwawqwddaeeead", HexDir.SOUTH_WEST, OpDispense)
		register("smelt", "qwqqadadadewewewe", HexDir.SOUTH_EAST, OpCook(RecipeType.SMELTING, "target.smelting"))
		register("roast", "aqqwwqqawdadedad", HexDir.NORTH_WEST, OpCook(RecipeType.CAMPFIRE_COOKING, "target.roasting"))
		register("smoke", "qwqqadadadewdqqdwe", HexDir.SOUTH_EAST, OpCook(RecipeType.SMOKING, "target.smoking"))
		register("blast", "qwqqadadadewweewwe", HexDir.SOUTH_EAST, OpCook(RecipeType.BLASTING, "target.blasting"))
		register("stonecut", "qqqqqwaeaeaeaeaeadawa", HexDir.EAST, OpStonecut)

		register("displace", "qaqqqqeedaqqqa", HexDir.NORTH_EAST, OpDisplace)
		register("absorb_arm", "aaqqadaqwqa", HexDir.WEST, OpAbsorbArm)

		register("get_evocation", "wwdeeeeeqeaqawwewewwaqawwewew", HexDir.EAST, OpGetEvocation)
		register("set_evocation", "wwaqqqqqeqdedwwqwqwwdedwwqwqw", HexDir.EAST, OpSetEvocation)
		register("is_evoking", "wwaqqqqqeeaqawwewewwaqawwewew", HexDir.EAST, OpGetKeybindSelf("key.hexical.evoke"))

		register("set_lesser_sentinels", "aeaae", HexDir.EAST, OpLesserSentinelSet)
		register("get_lesser_sentinels", "dqddq", HexDir.WEST, OpLesserSentinelGet)

		register("list_possessions", "eeeewddwa", HexDir.EAST, OpListPossessions)
		register("move_item", "qqqqwaawd", HexDir.EAST, OpMoveItem)
		register("absorb_item", "qqadawaawd", HexDir.EAST, OpAbsorbItem)
		register("expel_item", "eedadwddwa", HexDir.EAST, OpExpelItem)

		register("conjure_firework", "dedwaqwwawwqa", HexDir.SOUTH_WEST, OpConjureFirework)

		register("hunt_sentinel", "dwqwaeawaeqqqwqwqqw", HexDir.NORTH_EAST, OpHuntSentinel)
		register("exorcise_sentinel", "wdqdwdqedwewaawewd", HexDir.EAST, OpExorciseSentinel)

		register("shader_clear", "eeeeeqaqeeeee", HexDir.WEST, OpShader(null))
		register("shader_owl", "edewawede", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/night_vision.json")))
		register("shader_lines", "eedwwawwdee", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/outlines_only.json")))
		register("shader_tv", "wewdwewwawwewdwew", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/television.json")))
		register("shader_media", "eewdweqaqewdwee", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/media.json")))
		register("shader_spider", "qaqdedaedqqdedaqaedeqd", HexDir.NORTH_EAST, OpShader(HexicalMain.id("shaders/post/spider.json")))
		// color shift - edqdeqaqedqde

		register("horrible", "wedqawqeewdeaqeewdeaqqedqawqqedqawqeedqawqqewdeaqeedqawqeewdeaqqewdeaqeewdeaqeedqawqqedqawqqewdeaqeedqawqeewdeaqqewdeaqeewdeaqeedqawqqedqawqqewdeaqqedqawqeewdeaqeewdeaqqedqawqqedqawqeedqawqqewdeaqqedqawqeewdeaqeewdeaqqedqawqqedqawqeedqawqqewdeaqeedqawqeewdeaqeewdeaqqedqawqqedqawqeedqawqqewdeaqqedqawqeewdeaqqewdeaqeewdeaqeedqawqqedqawqqewdeaqe", HexDir.EAST, OpHorrible)

		register("conjure_speck", "ade", HexDir.SOUTH_WEST, OpConjureSpeck)

		register("kill_specklike", "adeaqde", HexDir.SOUTH_WEST, OpKillSpecklike)
		register("move_specklike", "adeqaa", HexDir.SOUTH_WEST, OpSetSpecklikePos)
		register("rotate_specklike", "adeaw", HexDir.SOUTH_WEST, OpSetSpecklikeRotation)
		register("roll_specklike", "adeqqqqq", HexDir.SOUTH_WEST, OpSetSpecklikeRoll)
		register("size_specklike", "adeeqed", HexDir.SOUTH_WEST, OpSetSpecklikeSize)
		register("thickness_specklike", "adeeqw", HexDir.SOUTH_WEST, OpSetSpecklikeThickness)
		register("lifetime_specklike", "adeqqaawdd", HexDir.SOUTH_WEST, OpSetSpecklikeLifespan)
		register("pigment_specklike", "adeqqaq", HexDir.SOUTH_WEST, OpSetSpecklikePigment)

		register("conjure_strand", "daq", HexDir.SOUTH_EAST, OpConjureStrand)
		register("draw_pattern", "eadqqqa", HexDir.NORTH_EAST, OpDrawPattern)

		register("conjure_mesh", "qaqqqqqwqqqdeeweweeaeewewee", HexDir.EAST, OpConjureMesh)

		register("charm", "edeeeeeqaaqeeeadweeqeeqdqeeqeeqde", HexDir.SOUTH_EAST, OpCharmItem)
		register("discharm", "qaqwddaaeawaea", HexDir.NORTH_EAST, OpDischarmItem)
		register("write_charmed", "edewqaqqdeeeee", HexDir.SOUTH_EAST, OpWriteCharmedProxy)
		register("read_charmed", "qaqwedeeaqqqqq", HexDir.NORTH_EAST, OpReadCharmedProxy)

		register("greater_blink", "wqawawaqwqwqawawaqw", HexDir.SOUTH_WEST, OpGreaterBlink)
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(HexActions.REGISTRY, HexicalMain.id(name), ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action))
}
