package miyucomics.hexical.inits

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.vecFromNBT
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntitiesBy
import at.petrak.hexcasting.common.casting.operators.stack.OpTwiddling
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.iota.IdentifierIota
import miyucomics.hexical.casting.iota.asActionResult
import miyucomics.hexical.casting.patterns.*
import miyucomics.hexical.casting.patterns.akashic.OpClearAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpKeyAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpReadAkashicShelf
import miyucomics.hexical.casting.patterns.akashic.OpWriteAkashicShelf
import miyucomics.hexical.casting.patterns.circle.OpDisplace
import miyucomics.hexical.casting.patterns.colors.OpDye
import miyucomics.hexical.casting.patterns.colors.OpGetDye
import miyucomics.hexical.casting.patterns.colors.OpTranslateDye
import miyucomics.hexical.casting.patterns.conjure.OpConjureCompass
import miyucomics.hexical.casting.patterns.conjure.OpConjureHexburst
import miyucomics.hexical.casting.patterns.conjure.OpConjureHextito
import miyucomics.hexical.casting.patterns.conjure.OpConjureSpike
import miyucomics.hexical.casting.patterns.eval.*
import miyucomics.hexical.casting.patterns.firework.OpConjureFirework
import miyucomics.hexical.casting.patterns.firework.OpSimulateFirework
import miyucomics.hexical.casting.patterns.getters.*
import miyucomics.hexical.casting.patterns.getters.misc.OpGetEnchantmentStrength
import miyucomics.hexical.casting.patterns.getters.misc.OpGetStatusEffectCategory
import miyucomics.hexical.casting.patterns.getters.types.OpGetBlockTypeData
import miyucomics.hexical.casting.patterns.getters.types.OpGetFoodTypeData
import miyucomics.hexical.casting.patterns.getters.types.OpGetItemTypeData
import miyucomics.hexical.casting.patterns.grimoire.OpGrimoireErase
import miyucomics.hexical.casting.patterns.grimoire.OpGrimoireIndex
import miyucomics.hexical.casting.patterns.grimoire.OpGrimoireWrite
import miyucomics.hexical.casting.patterns.identifier.OpIdentify
import miyucomics.hexical.casting.patterns.identifier.OpRecognize
import miyucomics.hexical.casting.patterns.lamp.*
import miyucomics.hexical.casting.patterns.mage_blocks.OpConjureMageBlock
import miyucomics.hexical.casting.patterns.mage_blocks.OpModifyMageBlock
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
import miyucomics.hexical.casting.patterns.soroban.OpSorobanDecrement
import miyucomics.hexical.casting.patterns.soroban.OpSorobanIncrement
import miyucomics.hexical.casting.patterns.soroban.OpSorobanReset
import miyucomics.hexical.casting.patterns.specks.*
import miyucomics.hexical.casting.patterns.staff.OpConjureStaff
import miyucomics.hexical.casting.patterns.staff.OpReadStaff
import miyucomics.hexical.casting.patterns.staff.OpWriteStaff
import miyucomics.hexical.casting.patterns.telepathy.OpHallucinateSound
import miyucomics.hexical.casting.patterns.telepathy.OpSendTelepathy
import miyucomics.hexical.casting.patterns.telepathy.OpShoutTelepathy
import miyucomics.hexical.casting.patterns.wristpocket.*
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.Specklike
import miyucomics.hexical.items.HandLampItem
import net.minecraft.block.CandleBlock
import net.minecraft.block.SeaPickleBlock
import net.minecraft.block.TurtleEggBlock
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry

object HexicalPatterns {
	@JvmStatic
	fun init() {
		register("offer_mind", "qaqwawqwqqwqwqwqwqwqq", HexDir.EAST, OpOfferMind())
		register("educate_genie", "eweweweweweewedeaqqqd", HexDir.NORTH_WEST, OpEducateGenie())
		register("get_hand_lamp_position", "qwddedqdd", HexDir.SOUTH_WEST, OpGetHandLampData { _, nbt -> vecFromNBT(nbt.getLongArray("position")).asActionResult })
		register("get_hand_lamp_rotation", "qwddedadw", HexDir.SOUTH_WEST, OpGetHandLampData { _, nbt -> vecFromNBT(nbt.getLongArray("rotation")).asActionResult })
		register("get_hand_lamp_velocity", "qwddedqew", HexDir.SOUTH_WEST, OpGetHandLampData { _, nbt -> vecFromNBT(nbt.getLongArray("velocity")).asActionResult })
		register("get_hand_lamp_use_time", "qwddedqwddwa", HexDir.SOUTH_WEST, OpGetHandLampData { ctx, nbt -> (ctx.world.time - (nbt.getDouble("start_time") + 1.0)).asActionResult })
		register("get_hand_lamp_media", "qwddedaeeeee", HexDir.SOUTH_WEST, OpGetHandLampData { ctx, _ -> ((ctx.caster.activeItem.item as HandLampItem).getMedia(ctx.caster.activeItem).toDouble() / MediaConstants.DUST_UNIT).asActionResult })
		register("get_hand_lamp_storage", "qwddedqwaqqqqq", HexDir.SOUTH_WEST, OpGetHandLampData { ctx, nbt -> listOf(HexIotaTypes.deserialize(nbt.getCompound("storage"), ctx.world)) })
		register("set_hand_lamp_storage", "qwddedqedeeeee", HexDir.SOUTH_WEST, OpSetHandLampStorage())
		register("get_arch_lamp_position", "qaqwddedqdd", HexDir.NORTH_EAST, OpGetArchLampData { _, data -> data.position.asActionResult } )
		register("get_arch_lamp_rotation", "qaqwddedadw", HexDir.NORTH_EAST, OpGetArchLampData { _, data -> data.rotation.asActionResult } )
		register("get_arch_lamp_velocity", "qaqwddedqew", HexDir.NORTH_EAST, OpGetArchLampData { _, data -> data.velocity.asActionResult } )
		register("get_arch_lamp_use_time", "qaqwddedqwddwa", HexDir.NORTH_EAST, OpGetArchLampData { ctx, data -> (ctx.world.time - (data.time + 1)).asActionResult } )
		register("get_arch_lamp_storage", "qaqwddedqwaqqqqq", HexDir.NORTH_EAST, OpGetArchLampData { ctx, data -> listOf(HexIotaTypes.deserialize(data.storage, ctx.world)) } )
		register("set_arch_lamp_storage", "qaqwddedqedeeeee", HexDir.NORTH_EAST, OpSetArchLampStorage())
		register("get_arch_lamp_media", "qaqwddedaeeeee", HexDir.NORTH_EAST, OpGetArchLampMedia())
		register("activate_arch_lamp", "qaqwddedadeaqq", HexDir.NORTH_EAST, OpActivateArchLamp())
		register("terminate_arch_lamp", "qaqwddedwaqdee", HexDir.NORTH_EAST, OpTerminateArchLamp())
		register("has_arch_lamp", "qaqwddedqeed", HexDir.NORTH_EAST, OpIsUsingArchLamp())
		register("lamp_finale", "aaddaddad", HexDir.EAST, OpGetFinale())

		register("age_scroll", "waeqqqqeqqqwqeaeaeaeq", HexDir.EAST, OpAgeScroll())
		register("color_scroll", "waeqqqqewqqwqqeqeqqwqqeq", HexDir.EAST, OpColorScroll())
		register("glow_scroll", "waeqqqqedeqdqdqdqeqdwwd", HexDir.EAST, OpGlowScroll())
		register("vanish_scroll", "waeqqqqedeqeeweeqewee", HexDir.EAST, OpVanishScroll())

		register("key_shelf", "qaqqadaq", HexDir.EAST, OpKeyAkashicShelf())
		register("read_shelf", "qaqqqada", HexDir.EAST, OpReadAkashicShelf())
		register("write_shelf", "edeeedad", HexDir.SOUTH_WEST, OpWriteAkashicShelf())
		register("clear_shelf", "edeedade", HexDir.SOUTH_WEST, OpClearAkashicShelf())

		register("displace", "qaqqqqeedaqqqa", HexDir.NORTH_EAST, OpDisplace())

		register("swap_one_three", "ddwqaq", HexDir.NORTH_EAST, OpTwiddling(3, intArrayOf(2, 1, 0)))
		register("swap_two_three", "aawede", HexDir.EAST, OpTwiddling(3, intArrayOf(1, 0, 2)))
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
		register("entity_width", "dwe", HexDir.NORTH_WEST, OpGetEntityData { entity -> entity.width.asActionResult })
		register("similar", "dew", HexDir.NORTH_WEST, OpSimilar())
		register("congruent", "aaqd", HexDir.EAST, OpCongruentPattern())
		register("dup_many", "waadadaa", HexDir.EAST, OpDupMany())
		register("shuffle_pattern", "aqqqdae", HexDir.NORTH_EAST, OpShufflePattern())

		register("soroban_decrement", "waqdee", HexDir.SOUTH_EAST, OpSorobanDecrement())
		register("soroban_increment", "wdeaqq", HexDir.NORTH_EAST, OpSorobanIncrement())
		register("soroban_reset", "qdeeaae", HexDir.NORTH_EAST, OpSorobanReset())

		register("fluid_raycast", "wqqaqwede", HexDir.EAST, OpFluidRaycast())
		register("fluid_surface_raycast", "weedewqaq", HexDir.EAST, OpFluidSurfaceRaycast())
		register("piercing_raycast", "wqqddqeqddq", HexDir.EAST, OpPiercingRaycast())
		register("piercing_surface_raycast", "weeaaeqeaae", HexDir.EAST, OpPiercingSurfaceRaycast())

		register("get_telepathy", "wqqadaw", HexDir.EAST, OpGetKeybind("key.hexical.telepathy"))
		register("send_telepathy", "qqqqwaqa", HexDir.EAST, OpSendTelepathy())
		register("shout_telepathy", "daqqqqwa", HexDir.EAST, OpShoutTelepathy())
		register("pling", "eqqqada", HexDir.NORTH_EAST, OpHallucinateSound(SoundEvents.ENTITY_PLAYER_LEVELUP))
		register("click", "eqqadaq", HexDir.NORTH_EAST, OpHallucinateSound(SoundEvents.UI_BUTTON_CLICK))
		register("moving_left", "edead", HexDir.SOUTH_EAST, OpGetKeybind("key.left"))
		register("moving_right", "qaqda", HexDir.SOUTH_WEST, OpGetKeybind("key.right"))
		register("moving_up", "aqaddq", HexDir.SOUTH_EAST, OpGetKeybind("key.forward"))
		register("moving_down", "dedwdq", HexDir.SOUTH_WEST, OpGetKeybind("key.back"))
		register("jumping", "qaqdaqqa", HexDir.SOUTH_WEST, OpGetKeybind("key.jump"))

		register("perlin", "qawedqdq", HexDir.WEST, OpPerlin())

		register("am_enlightened", "awqaqqq", HexDir.SOUTH_EAST, OpEnlightened())
		register("is_brainswept", "qqqaqqq", HexDir.SOUTH_EAST, OpBrainswept())

		register("autograph", "wwqqqqq", HexDir.NORTH_EAST, OpAutograph())
		register("conjure_hexburst", "aadaadqaq", HexDir.EAST, OpConjureHexburst())
		register("conjure_hextito", "qaqdqaqdwawaw", HexDir.EAST, OpConjureHextito())
		register("ghast_fireball", "wqqqqqwaeaeaeaeae", HexDir.SOUTH_EAST, OpGhastFireball())
		register("chorus_blink", "aawqqqq", HexDir.SOUTH_EAST, OpChorusBlink())
		register("gasp", "aweeeeewaweeeee", HexDir.NORTH_WEST, OpGasp())

		register("get_dye", "weedwa", HexDir.NORTH_EAST, OpGetDye())
		register("dye", "dwaqqw", HexDir.NORTH_WEST, OpDye())
		register("translate_dye", "wdwwaawwewdwwewwdwwe", HexDir.EAST, OpTranslateDye())

		register("to_pigment", "aqwedeweeeewweeew", HexDir.NORTH_WEST, OpToPigment())
		register("sample_pigment", "edewqaqqqqqwqqq", HexDir.SOUTH_EAST, OpSamplePigment())
		register("take_on_pigment", "weeeweeqeeeewqaqweeee", HexDir.EAST, OpTakeOnPigment())

		register("wristpocket", "aaqqa", HexDir.WEST, OpWristpocket())
		register("wristpocket_item", "aaqqada", HexDir.WEST, OpGetWristpocket { stack -> if (stack.isOf(Items.AIR) || stack == ItemStack.EMPTY) listOf(NullIota()) else Registry.ITEM.getId(stack.item).asActionResult() })
		register("wristpocket_count", "aaqqaaw", HexDir.WEST, OpGetWristpocket { stack -> if (stack.isOf(Items.AIR) || stack == ItemStack.EMPTY) (0).asActionResult else stack.count.asActionResult })
		register("sleight", "aaqqadeeeq", HexDir.WEST, OpSleight())
		register("mage_hand", "aaqqaeea", HexDir.WEST, OpMageHand())
		register("ingest", "aaqqadaa", HexDir.WEST, OpIngest())

		register("prestidigitation", "wedewedew", HexDir.NORTH_EAST, OpPrestidigitation())
		register("can_prestidigitate", "wqaqwqaqw", HexDir.NORTH_WEST, OpCanPrestidigitation())

		register("magic_missile", "qaqww", HexDir.WEST, OpMagicMissile())

		register("conjure_compass", "aqwawqwqqwqwq", HexDir.SOUTH_WEST, OpConjureCompass())

		register("spike", "qdqdqdqdww", HexDir.NORTH_EAST, OpConjureSpike())

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

		register("conjure_mage_block", "dee", HexDir.NORTH_WEST, OpConjureMageBlock())
		register("modify_block_bouncy", "deeqa", HexDir.NORTH_WEST, OpModifyMageBlock("bouncy"))
		register("modify_block_energized", "deewad", HexDir.NORTH_WEST, OpModifyMageBlock("energized", 1))
		register("modify_block_ephemeral", "deewwaawd", HexDir.NORTH_WEST, OpModifyMageBlock("ephemeral", 1))
		register("modify_block_invisible", "deeqedeaqqqwqqq", HexDir.NORTH_WEST, OpModifyMageBlock("invisible"))
		register("modify_block_replaceable", "deewqaqqqqq", HexDir.NORTH_WEST, OpModifyMageBlock("replaceable"))
		register("modify_block_semipermeable", "deeeqawde", HexDir.NORTH_WEST, OpModifyMageBlock("semipermeable"))
		register("modify_block_volatile", "deewedeeeee", HexDir.NORTH_WEST, OpModifyMageBlock("volatile"))

		register("conjure_staff", "wwwwwaqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpConjureStaff())
		register("write_staff", "waqqqqqedeqdqdqdqdqe", HexDir.NORTH_EAST, OpWriteStaff())
		register("read_staff", "waqqqqqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpReadStaff())

		register("conjure_firework", "dedwaqwwawwqa", HexDir.SOUTH_WEST, OpConjureFirework())
		register("simulate_firework", "dedwaqwqqwqa", HexDir.SOUTH_WEST, OpSimulateFirework())

		registerPerWorld("greater_blink", "wqawawaqwqwqawawaqw", HexDir.SOUTH_WEST, OpGreaterBlink())

		registerPerWorld("conjure_mesh", "qaqqqqqwqqqdeeweweeaeewewee", HexDir.EAST, OpConjureMesh())
		register("weave_mesh", "qaqqqqqwqqqdeewewee", HexDir.EAST, OpWeaveMesh())
		register("read_mesh", "edeeeeeweeeaqqwqwqq", HexDir.SOUTH_WEST, OpReadMesh())

		register("internalize_hex", "wwaqqqqqeqdedwwqwqwwdedwwqwqw", HexDir.EAST, OpInternalizeHex())
		register("is_evoking", "wwaqqqqqeeaqawwewewwaqawwewew", HexDir.EAST, OpGetKeybind("key.hexical.evoke"))

		register("with_hand_lamp", "qwddedqqaqqqqq", HexDir.SOUTH_WEST, OpCheckSource(SpecializedSource.HAND_LAMP))
		register("with_arch_lamp", "qaqwddedqqaqqqqq", HexDir.NORTH_EAST, OpCheckSource(SpecializedSource.ARCH_LAMP))
		register("with_conjured_staff", "waqaeaqeaqeaeaeaeaeq", HexDir.NORTH_EAST, OpCheckSource(SpecializedSource.CONJURED_STAFF))
		register("with_evocation", "waeqqqqedeqdqdqdqewee", HexDir.EAST, OpCheckSource(SpecializedSource.EVOCATION))

		register("write_grimoire", "aqwqaeaqa", HexDir.WEST, OpGrimoireWrite())
		register("erase_grimoire", "aqwqaqded", HexDir.WEST, OpGrimoireErase())
		register("index_grimoire", "aqaeaqwqa", HexDir.SOUTH_EAST, OpGrimoireIndex())

		register("identify", "qqqqqe", HexDir.NORTH_EAST, OpIdentify())
		register("recognize", "eeeeeq", HexDir.WEST, OpRecognize())
		register("get_mainhand_stack", "qaqqqq", HexDir.NORTH_EAST, OpGetPlayerData { player -> listOf(if (player.mainHandStack.isEmpty) NullIota() else IdentifierIota(Registry.ITEM.getId(player.mainHandStack.item))) })
		register("get_offhand_stack", "edeeee", HexDir.NORTH_WEST, OpGetPlayerData { player -> listOf(if (player.offHandStack.isEmpty) NullIota() else IdentifierIota(Registry.ITEM.getId(player.offHandStack.item))) })
		register("get_weather", "eweweweweweeeaedqdqde", HexDir.WEST, OpGetWorldData { world -> (
				if (world.isThundering) 2.0
				else if (world.isRaining) 1.0
				else 0.0
			).asActionResult
		})
		register("get_dimension", "qwqwqwqwqwqqaedwaqd", HexDir.WEST, OpGetWorldData { world -> world.registryKey.value.asActionResult() })
		register("get_time", "wddwaqqwqaddaqqwddwaqqwqaddaq", HexDir.SOUTH_EAST, OpGetWorldData { world -> (world.time.toDouble() / 20).asActionResult })
		register("get_light", "wqwqwqwqwqwaeqqqqaeqaeaeaeaw", HexDir.SOUTH_WEST, OpGetPositionData { world, position -> world.getLightLevel(position).asActionResult })
		register("get_biome", "qwqwqawdqqaqqdwaqwqwq", HexDir.WEST, OpGetPositionData { world, position -> world.getBiome(position).key.get().value.asActionResult() })
		register("count_stack", "qaqqwqqqw", HexDir.EAST, OpGetItemStackData { stack -> stack.count.asActionResult })
		register("damage_stack", "eeweeewdeq", HexDir.NORTH_EAST, OpGetItemStackData { stack -> stack.damage.asActionResult })

		register("block_hardness", "qaqqqqqeeeeedq", HexDir.EAST, OpGetBlockTypeData { block -> block.hardness.asActionResult })
		register("block_blast_resistance", "qaqqqqqewaaqddqa", HexDir.EAST, OpGetBlockTypeData { block -> block.blastResistance.asActionResult })
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
				enchantments.add(IdentifierIota(Registry.ENCHANTMENT.getId(enchantment)!!))
			enchantments.asActionResult
		})
		register("get_enchantment_strength", "waqwwqaweede", HexDir.WEST, OpGetEnchantmentStrength())

		register("get_hunger", "adaqqqddqe", HexDir.WEST, OpGetFoodTypeData { food -> food.hunger.asActionResult })
		register("get_saturation", "adaqqqddqw", HexDir.WEST, OpGetFoodTypeData { food -> food.saturationModifier.asActionResult })
		register("is_meat", "adaqqqddaed", HexDir.WEST, OpGetFoodTypeData { food -> food.isMeat.asActionResult })
		register("is_snack", "adaqqqddaq", HexDir.WEST, OpGetFoodTypeData { food -> food.isSnack.asActionResult })
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

		register("count_max_stack", "edeeweeew", HexDir.WEST, OpGetItemTypeData { item -> item.maxCount.asActionResult })
		register("damage_max_stack", "qqwqqqwaqe", HexDir.NORTH_WEST, OpGetItemTypeData { item -> item.maxDamage.asActionResult })
		register("edible", "adaqqqdd", HexDir.WEST, OpGetItemTypeData { item -> item.isFood.asActionResult })

		register("get_effects_entity", "wqqq", HexDir.SOUTH_WEST, OpGetLivingEntityData { entity ->
			val list = mutableListOf<Iota>()
			for (effect in entity.statusEffects)
				list.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
			list.asActionResult
		})
		register("get_effects_item", "wqqqadee", HexDir.SOUTH_WEST, OpGetPrescription())
		register("get_effect_category", "wqqqaawd", HexDir.SOUTH_WEST, OpGetStatusEffectCategory())
		register("get_effect_amplifier", "wqqqaqwa", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { instance -> instance.amplifier.asActionResult })
		register("get_effect_duration", "wqqqaqwdd", HexDir.SOUTH_WEST, OpGetStatusEffectInstanceData { instance -> (instance.duration.toDouble() / 20.0).asActionResult })

		register("atalanta", "aqdea", HexDir.SOUTH_WEST, OpAtalanta)
		register("castor", "adadee", HexDir.NORTH_WEST, OpCastor)
		register("pollux", "dadaqq", HexDir.NORTH_EAST, OpPollux)
		register("janus", "aadee", HexDir.SOUTH_WEST, OpJanus)
		register("sisyphus", "qaqwede", HexDir.NORTH_EAST, OpSisyphus)
		register("themis", "dwaad", HexDir.WEST, OpThemis)
		PatternRegistry.addSpecialHandler(HexicalMain.id("nephthys")) { pat ->
			val sig = pat.anglesSignature()
			if (sig.startsWith("deaqqd")) {
				var depth = 1
				sig.substring(6).forEachIndexed { index, char ->
					if (char != "qe"[index % 2])
						return@addSpecialHandler null
					depth += 1
				}
				return@addSpecialHandler OpNephthys(depth)
			}
			return@addSpecialHandler null
		}
		PatternRegistry.addSpecialHandler(HexicalMain.id("sekhmet")) { pat ->
			val sig = pat.anglesSignature()
			if (sig.startsWith("qaqdd")) {
				var depth = 0
				sig.substring(5).forEachIndexed { index, char ->
					if (char != "qe"[index % 2])
						return@addSpecialHandler null
					depth += 1
				}
				return@addSpecialHandler OpSekhmet(depth)
			}
			return@addSpecialHandler null
		}
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) = PatternRegistry.mapPattern(HexPattern.fromAngles(signature, startDir), HexicalMain.id(name), action)
	private fun registerPerWorld(name: String, signature: String, startDir: HexDir, action: Action) = PatternRegistry.mapPattern(HexPattern.fromAngles(signature, startDir), HexicalMain.id(name), action, true)
}