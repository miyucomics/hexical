package miyucomics.hexical.inits

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.vecFromNBT
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntitiesBy
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.environments.*
import miyucomics.hexical.casting.iotas.IdentifierIota
import miyucomics.hexical.casting.iotas.asActionResult
import miyucomics.hexical.casting.patterns.*
import miyucomics.hexical.casting.patterns.akashic.OpClearAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpKeyAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpReadAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpWriteAkashicShelf
import miyucomics.hexical.casting.patterns.circle.OpDisplace
import miyucomics.hexical.casting.patterns.colors.OpDye
import miyucomics.hexical.casting.patterns.colors.OpGetDye
import miyucomics.hexical.casting.patterns.colors.OpTranslateDye
import miyucomics.hexical.casting.patterns.conjure.*
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
import miyucomics.hexical.casting.patterns.scrying.*
import miyucomics.hexical.casting.patterns.scrying.identifier.OpClassify
import miyucomics.hexical.casting.patterns.scrying.identifier.OpIdentify
import miyucomics.hexical.casting.patterns.scrying.identifier.OpRecognize
import miyucomics.hexical.casting.patterns.scrying.types.OpGetBlockTypeData
import miyucomics.hexical.casting.patterns.scrying.types.OpGetFoodTypeData
import miyucomics.hexical.casting.patterns.scrying.types.OpGetItemTypeData
import miyucomics.hexical.casting.patterns.specks.*
import miyucomics.hexical.casting.patterns.tchotchke.OpConjureTchotchke
import miyucomics.hexical.casting.patterns.tchotchke.OpReadTchotchke
import miyucomics.hexical.casting.patterns.tchotchke.OpWriteTchotchke
import miyucomics.hexical.casting.patterns.telepathy.OpHallucinateSound
import miyucomics.hexical.casting.patterns.telepathy.OpSendTelepathy
import miyucomics.hexical.casting.patterns.telepathy.OpShoutTelepathy
import miyucomics.hexical.casting.patterns.wristpocket.*
import miyucomics.hexical.interfaces.Specklike
import miyucomics.hexical.items.HandLampItem
import net.minecraft.block.CandleBlock
import net.minecraft.block.SeaPickleBlock
import net.minecraft.block.TurtleEggBlock
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.math.Direction
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

		register("turret_lamp_normal", "qaqwddedweaqwqae", HexDir.NORTH_EAST, OpGetTurretNormal())

		register("am_enlightened", "awqaqqq", HexDir.SOUTH_EAST, OpEnlightened())
		register("is_brainswept", "qqqaqqq", HexDir.SOUTH_EAST, OpBrainswept())

		register("garbage", "aqawde", HexDir.EAST, Action.makeConstantOp(GarbageIota()))

		register("serialize_pattern", "wqaedeqd", HexDir.EAST, OpSerializePattern())
		register("deserialize_pattern", "wqqqaqwd", HexDir.EAST, OpDeserializePattern())
		register("congruent", "aaqd", HexDir.EAST, OpCongruentPattern())
		register("draw_pattern", "eadqqqa", HexDir.NORTH_EAST, OpDrawPattern())
		register("shuffle_pattern", "aqqqdae", HexDir.NORTH_EAST, OpShufflePattern())

		register("perlin", "qawedqdq", HexDir.WEST, OpPerlin())

		register("fluid_raycast", "wqqaqwede", HexDir.EAST, OpFluidRaycast())
		register("fluid_surface_raycast", "weedewqaq", HexDir.EAST, OpFluidSurfaceRaycast())
		register("piercing_raycast", "wqqddqeqddq", HexDir.EAST, OpPiercingRaycast())
		register("piercing_surface_raycast", "weeaaeqeaae", HexDir.EAST, OpPiercingSurfaceRaycast())

		register("get_telepathy", "wqqadaw", HexDir.EAST, OpGetKeybind("key.hexical.telepathy"))
		register("send_telepathy", "qqqqwaqa", HexDir.EAST, OpSendTelepathy())
		register("shout_telepathy", "daqqqqwa", HexDir.EAST, OpShoutTelepathy())
		register("pling", "eqqqada", HexDir.NORTH_EAST, OpHallucinateSound(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_PLAYER_LEVELUP)))
		register("click", "eqqadaq", HexDir.NORTH_EAST, OpHallucinateSound(Registries.SOUND_EVENT.getEntry(SoundEvents.UI_BUTTON_CLICK.comp_349())))
		register("moving_left", "edead", HexDir.SOUTH_EAST, OpGetKeybind("key.left"))
		register("moving_right", "qaqda", HexDir.SOUTH_WEST, OpGetKeybind("key.right"))
		register("moving_up", "aqaddq", HexDir.SOUTH_EAST, OpGetKeybind("key.forward"))
		register("moving_down", "dedwdq", HexDir.SOUTH_WEST, OpGetKeybind("key.back"))
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
		register("place_block_type", "wewewewewewdwew", HexDir.NORTH_WEST, OpConstruct())
		register("autograph", "wwqqqqq", HexDir.NORTH_EAST, OpAutograph())
		register("conjure_hexburst", "aadaadqaq", HexDir.EAST, OpConjureHexburst())
		register("conjure_hextito", "qaqdqaqdwawaw", HexDir.EAST, OpConjureHextito())
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

		register("internalize_hex", "wwaqqqqqeqdedwwqwqwwdedwwqwqw", HexDir.EAST, OpInternalizeHex())
		register("is_evoking", "wwaqqqqqeeaqawwewewwaqawwewew", HexDir.EAST, OpGetKeybind("key.hexical.evoke"))

		register("greater_blink", "wqawawaqwqwqawawaqw", HexDir.SOUTH_WEST, OpGreaterBlink())

		register("conjure_mesh", "qaqqqqqwqqqdeeweweeaeewewee", HexDir.EAST, OpConjureMesh())
		register("weave_mesh", "qaqqqqqwqqqdeewewee", HexDir.EAST, OpWeaveMesh())
		register("read_mesh", "edeeeeeweeeaqqwqwqq", HexDir.SOUTH_WEST, OpReadMesh())

		register("block_hardness", "qaqqqqqeeeeedq", HexDir.EAST, OpGetBlockTypeData { block -> block.hardness.asActionResult })
		register("block_blast_resistance", "qaqqqqqewaawaawa", HexDir.EAST, OpGetBlockTypeData { block -> block.blastResistance.asActionResult })
		register("blockstate_waterlogged", "edeeeeeqwqqqqw", HexDir.SOUTH_EAST, OpGetBlockStateData { state ->
			state.entries[Properties.WATERLOGGED] ?: return@OpGetBlockStateData listOf(NullIota())
            return@OpGetBlockStateData state.get(Properties.WATERLOGGED).asActionResult
		})
		register("blockstate_rotation", "qaqqqqqwadeeed", HexDir.EAST, OpGetBlockStateData { state ->
            return@OpGetBlockStateData if (state.entries[Properties.FACING] != null) state.get(Properties.FACING).unitVector.asActionResult
            else if (state.entries[Properties.HORIZONTAL_FACING] != null) state.get(Properties.HORIZONTAL_FACING).unitVector.asActionResult
            else if (state.entries[Properties.VERTICAL_DIRECTION] != null) state.get(Properties.VERTICAL_DIRECTION).unitVector.asActionResult
            else if (state.entries[Properties.AXIS] != null) Direction.from(state.get(Properties.AXIS), Direction.AxisDirection.POSITIVE).unitVector.asActionResult
            else if (state.entries[Properties.HORIZONTAL_AXIS] != null) Direction.from(state.get(Properties.HORIZONTAL_AXIS), Direction.AxisDirection.POSITIVE).unitVector.asActionResult
            else if (state.entries[Properties.HOPPER_FACING] != null) state.get(Properties.HOPPER_FACING).unitVector.asActionResult
            else listOf(NullIota())
		})
		register("blockstate_crop", "qaqqqqqwaea", HexDir.EAST, OpGetBlockStateData { state ->
            return@OpGetBlockStateData if (state.entries[Properties.AGE_1] != null) (state.get(Properties.AGE_1)).asActionResult
            else if (state.entries[Properties.AGE_2] != null) (state.get(Properties.AGE_2).toDouble() / 2.0).asActionResult
            else if (state.entries[Properties.AGE_3] != null) (state.get(Properties.AGE_3).toDouble() / 3.0).asActionResult
            else if (state.entries[Properties.AGE_4] != null) (state.get(Properties.AGE_4).toDouble() / 4.0).asActionResult
            else if (state.entries[Properties.AGE_5] != null) (state.get(Properties.AGE_5).toDouble() / 5.0).asActionResult
            else if (state.entries[Properties.AGE_7] != null) (state.get(Properties.AGE_7).toDouble() / 7.0).asActionResult
            else if (state.entries[Properties.AGE_15] != null) (state.get(Properties.AGE_15).toDouble() / 15.0).asActionResult
            else if (state.entries[Properties.AGE_25] != null) (state.get(Properties.AGE_25).toDouble() / 25.0).asActionResult
            else if (state.entries[Properties.LEVEL_3] != null) (state.get(Properties.LEVEL_3).toDouble() / 3).asActionResult
            else if (state.entries[Properties.LEVEL_8] != null) (state.get(Properties.LEVEL_8).toDouble() / 8).asActionResult
            else if (state.entries[Properties.HONEY_LEVEL] != null) (state.get(Properties.HONEY_LEVEL).toDouble() / 15.0).asActionResult
            else if (state.entries[Properties.BITES] != null) (state.get(Properties.BITES).toDouble() / 6.0).asActionResult
            else listOf(NullIota())
		})
		register("blockstate_glow", "qaqqqqqwaeaeaeaeaea", HexDir.EAST, OpGetBlockStateData { state ->
			state.entries[Properties.LIT] ?: return@OpGetBlockStateData listOf(NullIota())
			return@OpGetBlockStateData state.get(Properties.LIT).asActionResult
		})
		register("blockstate_lock", "qaqqqeaqwdewd", HexDir.EAST, OpGetBlockStateData { state ->
			state.entries[Properties.OPEN] ?: return@OpGetBlockStateData listOf(NullIota())
			return@OpGetBlockStateData state.get(Properties.OPEN).asActionResult
		})
		register("blockstate_turn", "qaqqqqqwqqwqd", HexDir.EAST, OpGetBlockStateData{ state ->
			state.entries[Properties.ROTATION] ?: return@OpGetBlockStateData listOf(NullIota())
			return@OpGetBlockStateData state.get(Properties.ROTATION).asActionResult
		})
		register("blockstate_bunch", "qaqqqqqweeeeedeeqaqdeee", HexDir.EAST, OpGetBlockStateData { state ->
			when (state.block) {
				is CandleBlock -> {
					state.entries[Properties.CANDLES] ?: return@OpGetBlockStateData listOf(NullIota())
					return@OpGetBlockStateData state.get(Properties.CANDLES).asActionResult
				}
				is SeaPickleBlock -> {
					state.entries[Properties.PICKLES] ?: return@OpGetBlockStateData listOf(NullIota())
                    return@OpGetBlockStateData state.get(Properties.PICKLES).asActionResult
				}
				is TurtleEggBlock -> {
					state.entries[Properties.EGGS] ?: return@OpGetBlockStateData listOf(NullIota())
					return@OpGetBlockStateData state.get(Properties.EGGS).asActionResult
				}
				else -> return@OpGetBlockStateData listOf(NullIota())
			}
		})
		register("blockstate_book", "qaqqqqqeawa", HexDir.EAST, OpGetBlockStateData { state ->
            return@OpGetBlockStateData if (state.entries[Properties.HAS_BOOK] != null) state.get(Properties.HAS_BOOK).asActionResult
            else if (state.entries[Properties.HAS_RECORD] != null) state.get(Properties.HAS_RECORD).asActionResult
            else if (state.entries[BlockAkashicBookshelf.HAS_BOOKS] != null) state.get(BlockAkashicBookshelf.HAS_BOOKS).asActionResult
            else listOf(NullIota())
		})

		register("get_enchantments", "waqeaeqawqwawaw", HexDir.WEST, OpGetItemStackData { stack ->
			var data = stack.enchantments
			if (stack.isOf(Items.ENCHANTED_BOOK))
				data = EnchantedBookItem.getEnchantmentNbt(stack)
			val enchantments = mutableListOf<IdentifierIota>()
			for ((enchantment, _) in EnchantmentHelper.fromNbt(data))
				enchantments.add(IdentifierIota(Registries.ENCHANTMENT.getId(enchantment)!!))
			enchantments.asActionResult
		})
		register("get_enchantment_strength", "waqwwqaweede", HexDir.WEST, OpGetEnchantmentStrength())

		register("entity_width", "dwe", HexDir.NORTH_WEST, OpGetEntityData { entity -> entity.width.asActionResult })
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
		register("is_burning", "qqwaqda", HexDir.EAST, OpGetEntityData { entity -> entity.isOnFire.asActionResult })
		register("burning_time", "eewdead", HexDir.WEST, OpGetEntityData { entity -> (entity.fireTicks.toDouble() / 20).asActionResult })
		register("is_wet", "qqqqwaadq", HexDir.SOUTH_WEST, OpGetEntityData { entity -> entity.isWet.asActionResult })
		register("get_health", "wddwaqqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData { entity -> entity.health.asActionResult })
		register("get_max_health", "wddwwawaeqwawq", HexDir.SOUTH_EAST, OpGetLivingEntityData { entity -> entity.maxHealth.asActionResult })
		register("get_air", "wwaade", HexDir.EAST, OpGetLivingEntityData { entity -> entity.air.asActionResult })
		register("get_max_air", "wwaadee", HexDir.EAST, OpGetLivingEntityData { entity -> entity.maxAir.asActionResult })
		register("is_sleeping", "aqaew", HexDir.NORTH_WEST, OpGetLivingEntityData { entity -> entity.isSleeping.asActionResult })
		register("is_sprinting", "eaq", HexDir.WEST, OpGetLivingEntityData { entity -> entity.isSprinting.asActionResult })
		register("is_baby", "awaqdwaaw", HexDir.SOUTH_WEST, OpGetLivingEntityData { entity -> entity.isBaby.asActionResult })
		register("breedable", "awaaqdqaawa", HexDir.EAST, OpGetWillingness())
		register("get_player_hunger", "qqqadaddw", HexDir.WEST, OpGetPlayerData { player -> player.hungerManager.foodLevel.asActionResult })
		register("get_player_saturation", "qqqadaddq", HexDir.WEST, OpGetPlayerData { player -> player.hungerManager.saturationLevel.asActionResult })

		register("env_ambit", "wawaw", HexDir.EAST, OpGetAmbit())
		register("env_staff", "waaq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is StaffCastEnv).asActionResult })
		register("env_offhand", "qaqqqwaaq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env.castingHand == Hand.MAIN_HAND).asActionResult })
		register("env_media", "dde", HexDir.WEST, OpGetEnvData { env -> ((Long.MAX_VALUE - env.extractMedia(Long.MAX_VALUE, true)).toDouble() / MediaConstants.DUST_UNIT.toDouble()).asActionResult })
		register("env_packaged_hex", "waaqwwaqqqqq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is PackagedItemCastEnv).asActionResult })
		register("env_circle", "waaqdeaqwqae", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is CircleCastEnv).asActionResult })
		register("env_turret", "waaqeqdewedq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is TurretLampCastEnv).asActionResult })
		register("env_hand_lamp", "waaqdqdded", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is HandLampCastEnv).asActionResult })
		register("env_arch_lamp", "waaqqqaqwdd", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is ArchLampCastEnv).asActionResult })
		register("env_evocation", "waaqeaqa", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is EvocationCastEnv).asActionResult })
		register("env_tchotchke", "waaqwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpGetEnvData { env -> (env is TchotchkeCastEnv).asActionResult })

		register("get_hunger", "adaqqqddqe", HexDir.WEST, OpGetFoodTypeData { food -> food.hunger.asActionResult })
		register("get_saturation", "adaqqqddqw", HexDir.WEST, OpGetFoodTypeData { food -> food.saturationModifier.asActionResult })
		register("is_meat", "adaqqqddaed", HexDir.WEST, OpGetFoodTypeData { food -> food.isMeat.asActionResult })
		register("is_snack", "adaqqqddaq", HexDir.WEST, OpGetFoodTypeData { food -> food.isSnack.asActionResult })

		register("identify", "qqqqqe", HexDir.NORTH_EAST, OpIdentify())
		register("recognize", "eeeeeq", HexDir.WEST, OpRecognize())
		register("classify", "edqdeq", HexDir.WEST, OpClassify())
		register("get_mainhand_stack", "qaqqqq", HexDir.NORTH_EAST, OpGetPlayerData { player -> listOf(if (player.mainHandStack.isEmpty) NullIota() else IdentifierIota(Registries.ITEM.getId(player.mainHandStack.item))) })
		register("get_offhand_stack", "edeeee", HexDir.NORTH_WEST, OpGetPlayerData { player -> listOf(if (player.offHandStack.isEmpty) NullIota() else IdentifierIota(Registries.ITEM.getId(player.offHandStack.item))) })

		register("count_stack", "qaqqwqqqw", HexDir.EAST, OpGetItemStackData { stack -> stack.count.asActionResult })
		register("count_max_stack", "edeeweeew", HexDir.WEST, OpGetItemTypeData { item -> item.maxCount.asActionResult })
		register("damage_stack", "eeweeewdeq", HexDir.NORTH_EAST, OpGetItemStackData { stack -> stack.damage.asActionResult })
		register("damage_max_stack", "qqwqqqwaqe", HexDir.NORTH_WEST, OpGetItemTypeData { item -> item.maxDamage.asActionResult })
		register("edible", "adaqqqdd", HexDir.WEST, OpGetItemTypeData { item -> item.isFood.asActionResult })

		register("get_effects_entity", "wqqq", HexDir.SOUTH_WEST, OpGetLivingEntityData { entity ->
			val list = mutableListOf<Iota>()
			for (effect in entity.statusEffects)
				list.add(IdentifierIota(Registries.STATUS_EFFECT.getId(effect.effectType)!!))
			list.asActionResult
		})
		register("get_effects_item", "wqqqadee", HexDir.SOUTH_WEST, OpGetPrescription())
		register("get_effect_category", "wqqqaawd", HexDir.SOUTH_WEST, OpGetStatusEffectCategory())
		register("get_effect_amplifier", "wqqqaqwa", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { instance -> instance.amplifier.asActionResult })
		register("get_effect_duration", "wqqqaqwdd", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { instance -> (instance.duration.toDouble() / 20.0).asActionResult })

		register("get_weather", "eweweweweweeeaedqdqde", HexDir.WEST, OpGetWorldData { world -> (
				if (world.isThundering) 2.0
				else if (world.isRaining) 1.0
				else 0.0
				).asActionResult
		})
		register("get_light", "wqwqwqwqwqwaeqqqqaeqaeaeaeaw", HexDir.SOUTH_WEST, OpGetPositionData { world, position -> world.getLightLevel(position).asActionResult })
		register("get_power", "qwqwqwqwqwqqwwaadwdaaww", HexDir.EAST, OpGetPositionData { world, position -> world.getReceivedRedstonePower(position).asActionResult })
		register("get_comparator", "eweweweweweewwddawaddww", HexDir.WEST, OpGetPositionData { world, position ->
			val state = world.getBlockState(position)
			if (state.hasComparatorOutput())
				return@OpGetPositionData state.getComparatorOutput(world, position).asActionResult
			return@OpGetPositionData listOf(NullIota())
		})
		register("get_day", "wwawwawwqqawwdwwdwwaqwqwqwqwq", HexDir.SOUTH_EAST, OpGetWorldData { world -> (world.timeOfDay.toDouble() / 24000.0).asActionResult })
		register("get_time", "wddwaqqwqaddaqqwddwaqqwqaddaq", HexDir.SOUTH_EAST, OpGetWorldData { world -> world.time.asActionResult })
		register("get_moon", "eweweweweweeweeedadw", HexDir.WEST, OpGetWorldData { world -> world.moonSize.asActionResult })
		register("get_biome", "qwqwqawdqqaqqdwaqwqwq", HexDir.WEST, OpGetPositionData { world, position -> world.getBiome(position).key.get().value.asActionResult() })
		register("get_dimension", "qwqwqwqwqwqqaedwaqd", HexDir.WEST, OpGetWorldData { world -> world.registryKey.value.asActionResult() })
		register("get_media", "ddew", HexDir.WEST, OpGetMedia())
		register("get_einstein", "aqwawqwqqwqwqwqwqwq", HexDir.SOUTH_WEST, OpGetWorldData { world -> world.dimension.comp_645().asActionResult })
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(HexActions.REGISTRY, HexicalMain.id(name), ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action))
}
