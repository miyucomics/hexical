package miyucomics.hexical.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.vecFromNBT
import at.petrak.hexcasting.common.casting.actions.local.OpPushLocal
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntitiesBy
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.patterns.*
import miyucomics.hexical.casting.patterns.akashic.OpClearAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpKeyAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpReadAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpWriteAkashicShelf
import miyucomics.hexical.casting.patterns.autograph.OpAutograph
import miyucomics.hexical.casting.patterns.autograph.OpUnautograph
import miyucomics.hexical.casting.patterns.circle.OpDisplace
import miyucomics.hexical.casting.patterns.colors.OpDye
import miyucomics.hexical.casting.patterns.colors.OpGetDye
import miyucomics.hexical.casting.patterns.colors.OpTranslateDye
import miyucomics.hexical.casting.patterns.conjure.*
import miyucomics.hexical.casting.patterns.evocation.OpGetEvocation
import miyucomics.hexical.casting.patterns.evocation.OpSetEvocation
import miyucomics.hexical.casting.patterns.firework.OpConjureFirework
import miyucomics.hexical.casting.patterns.firework.OpSimulateFirework
import miyucomics.hexical.casting.patterns.grimoire.OpGrimoireErase
import miyucomics.hexical.casting.patterns.grimoire.OpGrimoireIndex
import miyucomics.hexical.casting.patterns.grimoire.OpGrimoireWrite
import miyucomics.hexical.casting.patterns.lamp.*
import miyucomics.hexical.casting.patterns.mage_blocks.OpConjureMageBlock
import miyucomics.hexical.casting.patterns.mage_blocks.OpModifyMageBlock
import miyucomics.hexical.casting.patterns.pattern_manipulation.*
import miyucomics.hexical.casting.patterns.pigments.OpSamplePigment
import miyucomics.hexical.casting.patterns.pigments.OpTakeOnPigment
import miyucomics.hexical.casting.patterns.pigments.OpToPigment
import miyucomics.hexical.casting.patterns.prestidigitation.OpCanPrestidigitation
import miyucomics.hexical.casting.patterns.prestidigitation.OpPrestidigitation
import miyucomics.hexical.casting.patterns.raycast.OpFluidRaycast
import miyucomics.hexical.casting.patterns.raycast.OpFluidSurfaceRaycast
import miyucomics.hexical.casting.patterns.raycast.OpPiercingRaycast
import miyucomics.hexical.casting.patterns.raycast.OpPiercingSurfaceRaycast
import miyucomics.hexical.casting.patterns.scroll.OpAgeScroll
import miyucomics.hexical.casting.patterns.scroll.OpColorScroll
import miyucomics.hexical.casting.patterns.scroll.OpGlowScroll
import miyucomics.hexical.casting.patterns.scroll.OpVanishScroll
import miyucomics.hexical.casting.patterns.specks.*
import miyucomics.hexical.casting.patterns.tchotchke.OpConjureTchotchke
import miyucomics.hexical.casting.patterns.tchotchke.OpReadTchotchke
import miyucomics.hexical.casting.patterns.tchotchke.OpWriteTchotchke
import miyucomics.hexical.casting.patterns.telepathy.OpGetKeybind
import miyucomics.hexical.casting.patterns.telepathy.OpHallucinateSound
import miyucomics.hexical.casting.patterns.telepathy.OpSendTelepathy
import miyucomics.hexical.casting.patterns.telepathy.OpShoutTelepathy
import miyucomics.hexical.casting.patterns.vfx.OpBlockPing
import miyucomics.hexical.casting.patterns.vfx.OpConfetti
import miyucomics.hexical.casting.patterns.vfx.OpSparkle
import miyucomics.hexical.casting.patterns.wristpocket.*
import miyucomics.hexical.interfaces.Specklike
import miyucomics.hexical.items.HandLampItem
import miyucomics.hexposition.iotas.IdentifierIota
import miyucomics.hexposition.iotas.asActionResult
import miyucomics.hexposition.patterns.OpGetEntityData
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

object HexicalActions {
	@JvmStatic
	fun init() {
		register("age_scroll", "waeqqqqeqqqwqeaeaeaeq", HexDir.EAST, OpAgeScroll())
		register("color_scroll", "waeqqqqewqqwqqeqeqqwqqeq", HexDir.EAST, OpColorScroll())
		register("glow_scroll", "waeqqqqedeqdqdqdqeqdwwd", HexDir.EAST, OpGlowScroll())
		register("vanish_scroll", "waeqqqqedeqeeweeqewee", HexDir.EAST, OpVanishScroll())

		register("write_grimoire", "aqwqaeaqa", HexDir.WEST, OpGrimoireWrite())
		register("erase_grimoire", "aqwqaqded", HexDir.WEST, OpGrimoireErase())
		register("index_grimoire", "aqaeaqwqa", HexDir.SOUTH_EAST, OpGrimoireIndex())

		register("periwinkle", "qaqwqaqwqaq", HexDir.NORTH_EAST, Action.makeConstantOp(IdentifierIota(HexicalMain.id("periwinkle"))))

		register("wish", "eweweweweweewedeaqqqd", HexDir.NORTH_WEST, OpWish())
		register("recharge_lamp", "qaqwawqwqqwqwqwqwqwqq", HexDir.EAST, OpRechargeLamp())
		register("promote_lamp", "qweedeqeedeqdqdwewewwewewwewe", HexDir.WEST, OpPromoteLamp())
		register("get_hand_lamp_position", "qwddedqdd", HexDir.SOUTH_WEST, OpGetHandLampData { _, nbt -> vecFromNBT(nbt.getCompound("position")).asActionResult })
		register("get_hand_lamp_rotation", "qwddedadw", HexDir.SOUTH_WEST, OpGetHandLampData { _, nbt -> vecFromNBT(nbt.getCompound("rotation")).asActionResult })
		register("get_hand_lamp_velocity", "qwddedqew", HexDir.SOUTH_WEST, OpGetHandLampData { _, nbt -> vecFromNBT(nbt.getCompound("velocity")).asActionResult })
		register("get_hand_lamp_use_time", "qwddedqwddwa", HexDir.SOUTH_WEST, OpGetHandLampData { env, nbt -> (env.world.time - (nbt.getDouble("start_time") + 1.0)).asActionResult })
		register("get_hand_lamp_media", "qwddedaeeeee", HexDir.SOUTH_WEST, OpGetHandLampData { env, _ -> ((env.castingEntity!!.activeItem.item as HandLampItem).getMedia(env.castingEntity!!.activeItem).toDouble() / MediaConstants.DUST_UNIT).asActionResult })
		register("get_hand_lamp_storage", "qwddedqwaqqqqq", HexDir.SOUTH_WEST, OpGetHandLampData { env, nbt -> listOf(IotaType.deserialize(nbt.getCompound("storage"), env.world)) })
		register("set_hand_lamp_storage", "qwddedqedeeeee", HexDir.SOUTH_WEST, OpSetHandLampStorage())
		register("get_arch_lamp_position", "qaqwddedqdd", HexDir.NORTH_EAST, OpGetArchLampData { _, data -> data.position.asActionResult } )
		register("get_arch_lamp_rotation", "qaqwddedadw", HexDir.NORTH_EAST, OpGetArchLampData { _, data -> data.rotation.asActionResult } )
		register("get_arch_lamp_velocity", "qaqwddedqew", HexDir.NORTH_EAST, OpGetArchLampData { _, data -> data.velocity.asActionResult } )
		register("get_arch_lamp_use_time", "qaqwddedqwddwa", HexDir.NORTH_EAST, OpGetArchLampData { ctx, data -> (ctx.world.time - (data.time + 1)).asActionResult } )
		register("get_arch_lamp_storage", "qaqwddedqwaqqqqq", HexDir.NORTH_EAST, OpGetArchLampData { ctx, data -> listOf(IotaType.deserialize(data.storage, ctx.world)) } )
		register("set_arch_lamp_storage", "qaqwddedqedeeeee", HexDir.NORTH_EAST, OpSetArchLampStorage())
		register("get_arch_lamp_media", "qaqwddedaeeeee", HexDir.NORTH_EAST, OpGetArchLampMedia())
		register("has_arch_lamp", "qaqwddedqeed", HexDir.NORTH_EAST, OpIsUsingArchLamp())
		register("lamp_finale", "aaddaddad", HexDir.EAST, OpGetFinale())

		register("garbage", "aqawde", HexDir.EAST, Action.makeConstantOp(GarbageIota()))

		register("serialize_pattern", "wqaedeqd", HexDir.EAST, OpSerializePattern())
		register("deserialize_pattern", "wqqqaqwd", HexDir.EAST, OpDeserializePattern())
		register("congruent", "aaqd", HexDir.EAST, OpCongruentPattern())
		register("draw_pattern", "eadqqqa", HexDir.NORTH_EAST, OpDrawPattern())
		register("shuffle_pattern", "aqqqdae", HexDir.NORTH_EAST, OpShufflePattern())

		register("fluid_raycast", "wqqaqwede", HexDir.EAST, OpFluidRaycast())
		register("fluid_surface_raycast", "weedewqaq", HexDir.EAST, OpFluidSurfaceRaycast())
		register("piercing_raycast", "wqqddqeqddq", HexDir.EAST, OpPiercingRaycast())
		register("piercing_surface_raycast", "weeaaeqeaae", HexDir.EAST, OpPiercingSurfaceRaycast())

		register("get_telepathy", "wqqadaw", HexDir.EAST, OpGetKeybind("key.hexical.telepathy"))
		register("send_telepathy", "qqqqwaqa", HexDir.EAST, OpSendTelepathy())
		register("shout_telepathy", "daqqqqwa", HexDir.EAST, OpShoutTelepathy())
		register("pling", "eqqqada", HexDir.NORTH_EAST, OpHallucinateSound(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_PLAYER_LEVELUP)))
		register("click", "eqqadaq", HexDir.NORTH_EAST, OpHallucinateSound(Registries.SOUND_EVENT.getEntry(SoundEvents.UI_BUTTON_CLICK.comp_349())))
		register("left_click", "qadee", HexDir.NORTH_EAST, OpGetKeybind("key.attack"))
		register("right_click", "edaqq", HexDir.NORTH_WEST, OpGetKeybind("key.use"))
		register("moving_up", "aqaddq", HexDir.SOUTH_EAST, OpGetKeybind("key.forward"))
		register("moving_down", "dedwdq", HexDir.SOUTH_WEST, OpGetKeybind("key.back"))
		register("moving_left", "edead", HexDir.SOUTH_EAST, OpGetKeybind("key.left"))
		register("moving_right", "qaqda", HexDir.SOUTH_WEST, OpGetKeybind("key.right"))
		register("jumping", "qaqdaqqa", HexDir.SOUTH_WEST, OpGetKeybind("key.jump"))
		register("sneaking", "wede", HexDir.NORTH_WEST, OpGetKeybind("key.sneak"))

		register("key_shelf", "qaqqadaq", HexDir.EAST, OpKeyAkashicShelf())
		register("read_shelf", "qaqqqada", HexDir.EAST, OpReadAkashicShelf())
		register("write_shelf", "edeeedad", HexDir.SOUTH_WEST, OpWriteAkashicShelf())
		register("clear_shelf", "edeedade", HexDir.SOUTH_WEST, OpClearAkashicShelf())

		register("conjure_mage_block", "dee", HexDir.NORTH_WEST, OpConjureMageBlock())
		register("modify_block_bouncy", "deeqa", HexDir.NORTH_WEST, OpModifyMageBlock("bouncy"))
		register("modify_block_energized", "deewad", HexDir.NORTH_WEST, OpModifyMageBlock("energized", 1))
		register("modify_block_ephemeral", "deewwaawd", HexDir.NORTH_WEST, OpModifyMageBlock("ephemeral", 1))
		register("modify_block_invisible", "deeqedeaqqqwqqq", HexDir.NORTH_WEST, OpModifyMageBlock("invisible"))
		register("modify_block_replaceable", "deewqaqqqqq", HexDir.NORTH_WEST, OpModifyMageBlock("replaceable"))
		register("modify_block_volatile", "deewedeeeee", HexDir.NORTH_WEST, OpModifyMageBlock("volatile"))

		register("get_dye", "weedwa", HexDir.NORTH_EAST, OpGetDye())
		register("dye", "dwaqqw", HexDir.NORTH_WEST, OpDye())
		register("translate_dye", "wdwwaawwewdwwewwdwwe", HexDir.EAST, OpTranslateDye())

		register("magic_missile", "qaqww", HexDir.WEST, OpMagicMissile())

		register("to_pigment", "aqwedeweeeewweeew", HexDir.NORTH_WEST, OpToPigment())
		register("sample_pigment", "edewqaqqqqqwqqq", HexDir.SOUTH_EAST, OpSamplePigment())
		register("take_on_pigment", "weeeweeqeeeewqaqweeee", HexDir.EAST, OpTakeOnPigment())

		register("prestidigitation", "wedewedew", HexDir.NORTH_EAST, OpPrestidigitation())
		register("can_prestidigitate", "wqaqwqaqw", HexDir.NORTH_WEST, OpCanPrestidigitation())

		register("wristpocket", "aaqqa", HexDir.WEST, OpWristpocket())
		register("wristpocket_item", "aaqqada", HexDir.WEST, OpGetWristpocketData { stack -> if (stack.isEmpty) listOf(NullIota()) else Registries.ITEM.getId(stack.item).asActionResult() })
		register("wristpocket_count", "aaqqaaw", HexDir.WEST, OpGetWristpocketData { stack -> if (stack.isEmpty) (0).asActionResult else stack.count.asActionResult })
		register("sleight", "aaqqadeeeq", HexDir.WEST, OpSleight())
		register("mage_hand", "aaqqaeea", HexDir.WEST, OpMageHand())
		register("mage_mouth", "aaqqadaa", HexDir.WEST, OpMageMouth())

		register("conjure_speck", "ade", HexDir.SOUTH_WEST, OpConjureSpeck())
		register("iota_speck", "adeeaqa", HexDir.SOUTH_WEST, OpIotaSpeck())
		register("kill_specklike", "adeaqde", HexDir.SOUTH_WEST, OpKillSpecklike())
		register("move_specklike", "adeqaa", HexDir.SOUTH_WEST, OpSpecklikeProperty(0))
		register("rotate_specklike", "adeaw", HexDir.SOUTH_WEST, OpSpecklikeProperty(1))
		register("roll_specklike", "adeqqqqq", HexDir.SOUTH_WEST, OpSpecklikeProperty(2))
		register("size_specklike", "adeeqed", HexDir.SOUTH_WEST, OpSpecklikeProperty(3))
		register("thickness_specklike", "adeeqw", HexDir.SOUTH_WEST, OpSpecklikeProperty(4))
		register("lifetime_specklike", "adeqqaawdd", HexDir.SOUTH_WEST, OpSpecklikeProperty(5))
		register("pigment_specklike", "adeqqaq", HexDir.SOUTH_WEST, OpSpecklikeProperty(6))
		register("zone_specklike", "qqqqqwdeddwqde", HexDir.SOUTH_EAST, OpGetEntitiesBy({ entity -> entity is Specklike }, false))

		register("shader_clear", "eeeeeqaqeeeee", HexDir.WEST, OpShader(null))
		register("shader_owl", "edewawede", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/night_vision.json")))
		register("shader_lines", "eedwwawwdee", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/outlines_only.json")))
		register("shader_tv", "wewdwewwawwewdwew", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/television.json")))
		register("shader_media", "eewdweqaqewdwee", HexDir.WEST, OpShader(HexicalMain.id("shaders/post/media.json")))
		register("shader_spider", "qaqdedaedqqdedaqaedeqd", HexDir.NORTH_EAST, OpShader(HexicalMain.id("shaders/post/spider.json")))
		// color shift - edqdeqaqedqde

		register("confetti", "awddeqaedd", HexDir.EAST, OpConfetti())
		register("sparkle", "dqa", HexDir.NORTH_EAST, OpSparkle())
		register("block_ping", "dwwdwwdwewdwwdwwdeq", HexDir.NORTH_EAST, OpBlockPing())
		register("stonecut", "qqqqqwaeaeaeaeaeadawa", HexDir.EAST, OpStonecut())
		register("crack_device", "wwaqqqqqeqdedwqeaeqwdedwqeaeq", HexDir.EAST, OpCrackDevice())
		register("place_block_type", "wewewewewewdwew", HexDir.NORTH_WEST, OpConstruct())
		register("autograph", "eeeeeww", HexDir.WEST, OpAutograph())
		register("unautograph", "wwqqqqq", HexDir.NORTH_EAST, OpUnautograph())
		register("conjure_hexburst", "aadaadqaq", HexDir.EAST, OpConjureHexburst())
		register("conjure_hextito", "qaqdqaqdwawaw", HexDir.EAST, OpConjureHextito())
		register("flower", "weqqqqqwaeaeaeaeaea", HexDir.NORTH_EAST, OpConjureFlower())
		register("light", "aeaeaeaeaeawqqqqq", HexDir.SOUTH_EAST, OpConjureLight())
		register("snowball", "ddeeeeewd", HexDir.NORTH_EAST, OpSnowball())
		register("ghast_fireball", "wqqqqqwaeaeaeaeae", HexDir.SOUTH_EAST, OpGhastFireball())
		register("llama_spit", "dwqaqw", HexDir.EAST, OpLlamaSpit())
		register("gasp", "aweeeeewaweeeee", HexDir.NORTH_WEST, OpGasp())
		register("parrot", "wweedadw", HexDir.NORTH_EAST, OpImitateParrot())
		register("myodesopsia", "wadawadawawaaw", HexDir.SOUTH_EAST, OpMyodesopsia())

		register("conjure_compass", "aqwawqwqqwqwq", HexDir.SOUTH_WEST, OpConjureCompass())

		register("spike", "qdqdqdqdww", HexDir.NORTH_EAST, OpConjureSpike())

		register("conjure_tchotchke", "wwwwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpConjureTchotchke())
		register("write_tchotchke", "waqqqqqedeqdqdqdqdqe", HexDir.NORTH_EAST, OpWriteTchotchke())
		register("read_tchotchke", "waqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpReadTchotchke())

		register("conjure_firework", "dedwaqwwawwqa", HexDir.SOUTH_WEST, OpConjureFirework())
		register("simulate_firework", "dedwaqwqqwqa", HexDir.SOUTH_WEST, OpSimulateFirework())

		register("displace", "qaqqqqeedaqqqa", HexDir.NORTH_EAST, OpDisplace())

		register("get_evocation", "wwdeeeeeqeaqawwewewwaqawwewew", HexDir.EAST, OpGetEvocation())
		register("set_evocation", "wwaqqqqqeqdedwwqwqwwdedwwqwqw", HexDir.EAST, OpSetEvocation())
		register("is_evoking", "wwaqqqqqeeaqawwewewwaqawwewew", HexDir.EAST, OpGetKeybind("key.hexical.evoke"))

		register("greater_blink", "wqawawaqwqwqawawaqw", HexDir.SOUTH_WEST, OpGreaterBlink())
		register("theodolite", "wqaa", HexDir.EAST, OpGetEntityData { entity ->
			val upPitch = (-entity.pitch + 90) * (Math.PI.toFloat() / 180)
			val yaw = -entity.headYaw * (Math.PI.toFloat() / 180)
			val h = MathHelper.cos(yaw).toDouble()
			val j = MathHelper.cos(upPitch).toDouble()
			Vec3d(
				MathHelper.sin(yaw).toDouble() * j,
				MathHelper.sin(upPitch).toDouble(),
				h * j
			).asActionResult
		})

		register("conjure_mesh", "qaqqqqqwqqqdeeweweeaeewewee", HexDir.EAST, OpConjureMesh())
		register("weave_mesh", "qaqqqqqwqqqdeewewee", HexDir.EAST, OpWeaveMesh())
		register("read_mesh", "edeeeeeweeeaqqwqwqq", HexDir.SOUTH_WEST, OpReadMesh())
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(HexActions.REGISTRY, HexicalMain.id(name), ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action))
}
