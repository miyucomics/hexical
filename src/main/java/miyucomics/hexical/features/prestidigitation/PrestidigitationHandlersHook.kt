package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.block.*
import net.minecraft.block.entity.BeehiveBlockEntity
import net.minecraft.block.entity.BellBlockEntity
import net.minecraft.block.enums.Attachment
import net.minecraft.block.enums.ComparatorMode
import net.minecraft.entity.Shearable
import net.minecraft.entity.TntEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.passive.PandaEntity
import net.minecraft.entity.passive.PufferfishEntity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.ShovelItem
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.event.GameEvent

object PrestidigitationHandlersHook : InitHook() {
	private val PRESTIDIGITATION_HANDLER_KEY: RegistryKey<Registry<PrestidigitationHandler>> = RegistryKey.ofRegistry(HexicalMain.id("prestidigitation_handler"))
	val PRESTIDIGITATION_HANDLER: SimpleRegistry<PrestidigitationHandler> = FabricRegistryBuilder.createSimple(PRESTIDIGITATION_HANDLER_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()

	override fun init() {
		register("toggle_comparator", object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.COMPARATOR)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.COMPARATOR_MODE, when (state.get(Properties.COMPARATOR_MODE)) {
					ComparatorMode.COMPARE -> ComparatorMode.SUBTRACT
					ComparatorMode.SUBTRACT -> ComparatorMode.COMPARE
				}))
			}
		})

		register("carve_pumpkin", object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.PUMPKIN)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, Blocks.CARVED_PUMPKIN.defaultState.with(Properties.HORIZONTAL_FACING, Properties.HORIZONTAL_FACING.values.random()))
			}
		})

		register("axeing", object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = AxeItem.STRIPPED_BLOCKS.containsKey(getBlock(env, pos))
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, AxeItem.STRIPPED_BLOCKS[getBlock(env, pos)]!!.defaultState)
			}
		})

		register("pathing", object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = ShovelItem.PATH_STATES.containsKey(getBlock(env, pos))
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, ShovelItem.PATH_STATES[getBlock(env, pos)]!!)
			}
		})

		register("press_buttons", object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.BUTTONS)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				(state.block as ButtonBlock).powerOn(state, env.world, pos)
				(state.block as ButtonBlock).playClickSound(null, env.world, pos, true)
				env.world.emitGameEvent(null, GameEvent.BLOCK_ACTIVATE, pos)
			}
		})

		register("extinguish_fires", object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.FIRE)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				env.world.removeBlock(pos, false)
			}
		})

		register("create_soul_fire", object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS) && getBlockState(env, pos.up()).isAir
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos.up(), Blocks.SOUL_FIRE.defaultState)
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("pressure_pressure_plates"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.PRESSURE_PLATES)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.POWERED, !state.get(Properties.POWERED)))
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("drain_cauldrons"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.CAULDRONS)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, Blocks.CAULDRON.defaultState)
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("light_candle"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos): Boolean {
				val state = getBlockState(env, pos)
				return state.isIn(BlockTags.CANDLES) || state.isIn(BlockTags.CANDLE_CAKES) || state.isIn(BlockTags.CAMPFIRES)
			}

			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.LIT, !state.get(Properties.LIT)))
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("open_doors"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos): Boolean {
				val state = getBlockState(env, pos)
				return state.isIn(BlockTags.DOORS) || state.isIn(BlockTags.TRAPDOORS) || state.isIn(BlockTags.FENCE_GATES)
			}

			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.OPEN, !state.get(Properties.OPEN)))
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("steal_honey"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos): Boolean {
				val state = getBlockState(env, pos)
				return state.isIn(BlockTags.BEEHIVES) && state.get(BeehiveBlock.HONEY_LEVEL) == 5
			}

			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				env.world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1f, 1f)
				BeehiveBlock.dropHoneycomb(env.world, pos)
				(state.block as BeehiveBlock).takeHoney(env.world, state, pos, null, BeehiveBlockEntity.BeeState.BEE_RELEASED)
				env.world.emitGameEvent(null, GameEvent.SHEAR, pos)
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("play_note"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.NOTE_BLOCK)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				env.world.addSyncedBlockEvent(pos, Blocks.NOTE_BLOCK, 0, 0)
				env.world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, pos)
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("ring_bell"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.BELL)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				val facing = state.get(BellBlock.FACING)
				val ringDirection = when (state.get(BellBlock.ATTACHMENT)) {
					Attachment.SINGLE_WALL -> facing.rotateYClockwise()
					Attachment.DOUBLE_WALL -> facing.rotateYClockwise()
					else -> facing
				}
				(env.world.getBlockEntity(pos) as BellBlockEntity).activate(ringDirection)
				env.world.playSound(null, pos, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.0f)
				env.world.emitGameEvent(env.castingEntity, GameEvent.BLOCK_CHANGE, pos)
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("dispense"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlock(env, pos) is DispenserBlock
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				(state.block as DispenserBlock).scheduledTick(state, env.world as ServerWorld, pos, null)
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("prime_tnt"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.TNT)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				TntBlock.primeTnt(env.world, pos)
				env.world.removeBlock(pos, false)
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("learn_akashic"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = env.castingEntity is ServerPlayerEntity && getBlockState(env, pos).isOf(HexBlocks.AKASHIC_BOOKSHELF)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val caster = env.castingEntity as ServerPlayerEntity
				val nbt = (env.world.getBlockEntity(pos) as BlockEntityAkashicBookshelf).iotaTag ?: return
				CastingUtils.giveIota(caster, IotaType.deserialize(nbt, caster.serverWorld))
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("trigger_impetus"), object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = env.castingEntity is ServerPlayerEntity && getBlockState(env, pos).isIn(HexTags.Blocks.IMPETI)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				(env.world.getBlockEntity(pos) as BlockEntityAbstractImpetus).startExecution(env.castingEntity as ServerPlayerEntity)
			}
		})

		register("arm_stands", object : PrestidigitationHandlerEntity<ArmorStandEntity>(ArmorStandEntity::class.java) {
			override fun affect(env: CastingEnvironment, entity: ArmorStandEntity) {
				entity.setShowArms(!entity.shouldShowArms())
				entity.playSound(SoundEvents.ENTITY_ARMOR_STAND_PLACE, 1f, 1f)
			}
		})

		register("disarm_tnt", object : PrestidigitationHandlerEntity<TntEntity>(TntEntity::class.java) {
			override fun affect(env: CastingEnvironment, entity: TntEntity) {
				if (entity.world.getBlockState(entity.blockPos).isReplaceable) {
					entity.world.setBlockState(entity.blockPos, Blocks.TNT.defaultState)
					entity.world.updateNeighborsAlways(entity.blockPos, Blocks.TNT)
				}
				entity.discard()
			}
		})

		register("shear", object : PrestidigitationHandlerEntity<Shearable>(Shearable::class.java) {
			override fun affect(env: CastingEnvironment, entity: Shearable) {
				entity.sheared(SoundCategory.MASTER)
			}
		})

		register("milk_squids", object : PrestidigitationHandlerEntity<SquidEntity>(SquidEntity::class.java) {
			override fun affect(env: CastingEnvironment, entity: SquidEntity) {
				entity.squirt()
			}
		})

		register("pandas_sneeze", object : PrestidigitationHandlerEntity<PandaEntity>(PandaEntity::class.java) {
			override fun affect(env: CastingEnvironment, entity: PandaEntity) {
				entity.isSneezing = true
			}
		})

		register("detonate_creepers", object : PrestidigitationHandlerEntity<CreeperEntity>(CreeperEntity::class.java) {
			override fun affect(env: CastingEnvironment, entity: CreeperEntity) {
				if (entity.isIgnited) entity.dataTracker.set(CreeperEntity.IGNITED, false)
				else entity.ignite()
			}
		})

		register("puff_pufferfish", object : PrestidigitationHandlerEntity<PufferfishEntity>(PufferfishEntity::class.java) {
			override fun affect(env: CastingEnvironment, entity: PufferfishEntity) {
				if (entity.puffState != 2) {
					entity.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP, 1f, 1f)
					entity.inflateTicks = 0
					entity.deflateTicks = 0
					entity.puffState = 2
				}
			}
		})

		PrestidigitationBlockBooleans.init()
		PrestidigitationBlockTransformations.init()
	}

	fun register(name: String, handler: PrestidigitationHandler) {
		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id(name), handler)
	}
}