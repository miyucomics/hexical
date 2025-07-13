package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.utils.CastingUtils
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.block.*
import net.minecraft.block.entity.BeehiveBlockEntity
import net.minecraft.block.entity.BellBlockEntity
import net.minecraft.block.enums.Attachment
import net.minecraft.block.enums.ComparatorMode
import net.minecraft.entity.Entity
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

object PrestidigitationData {
	private val PRESTIDIGITATION_HANDLER_KEY: RegistryKey<Registry<PrestidigitationHandler>> = RegistryKey.ofRegistry(HexicalMain.id("prestidigitation_handler"))
	val PRESTIDIGITATION_HANDLER: SimpleRegistry<PrestidigitationHandler> = FabricRegistryBuilder.createSimple(PRESTIDIGITATION_HANDLER_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()

	fun init() {
		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("toggle_comparator"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isOf(Blocks.COMPARATOR))
					return false
				when (state.get(Properties.COMPARATOR_MODE)!!) {
					ComparatorMode.COMPARE -> env.world.setBlockState(position, state.with(Properties.COMPARATOR_MODE, ComparatorMode.SUBTRACT))
					ComparatorMode.SUBTRACT -> env.world.setBlockState(position, state.with(Properties.COMPARATOR_MODE, ComparatorMode.COMPARE))
				}
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("carve_pumpkin"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isOf(Blocks.PUMPKIN))
					return false
				env.world.setBlockState(position, Blocks.CARVED_PUMPKIN.defaultState.with(Properties.HORIZONTAL_FACING, Properties.HORIZONTAL_FACING.values.random()))
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("axeing"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!AxeItem.STRIPPED_BLOCKS.containsKey(state.block))
					return false
				env.world.setBlockState(position, AxeItem.STRIPPED_BLOCKS[state.block]!!.defaultState)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("pathing"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!ShovelItem.PATH_STATES.containsKey(state.block))
					return false
				env.world.setBlockState(position, ShovelItem.PATH_STATES[state.block])
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("press_buttons"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isIn(BlockTags.BUTTONS))
					return false
				(state.block as ButtonBlock).powerOn(state, env.world, position)
				(state.block as ButtonBlock).playClickSound(null, env.world, position, true)
				env.world.emitGameEvent(null, GameEvent.BLOCK_ACTIVATE, position)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("extinguish_fires"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isIn(BlockTags.FIRE))
					return false
				env.world.removeBlock(position, false)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("create_soul_fire"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS))
					return false
				if (!env.world.getBlockState(position.up()).isAir)
					return false
				env.world.setBlockState(position.up(), Blocks.SOUL_FIRE.defaultState)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("pressure_pressure_plates"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isIn(BlockTags.PRESSURE_PLATES))
					return false
				env.world.setBlockState(position, state.with(Properties.POWERED, !state.get(Properties.POWERED)))
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("drain_cauldrons"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isIn(BlockTags.CAULDRONS))
					return false
				env.world.setBlockState(position, Blocks.CAULDRON.defaultState)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("light_candle"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isIn(BlockTags.CANDLES) && !state.isIn(BlockTags.CANDLE_CAKES) && !state.isIn(BlockTags.CAMPFIRES))
					return false
				env.world.setBlockState(position, state.with(Properties.LIT, !state.get(Properties.LIT)))
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("open_doors"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (!state.isIn(BlockTags.DOORS) && !state.isIn(BlockTags.TRAPDOORS) && !state.isIn(BlockTags.FENCE_GATES))
					return false
				env.world.setBlockState(position, state.with(Properties.OPEN, !state.get(Properties.OPEN)))
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("steal_honey"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val blockState = env.world.getBlockState(position)
				if (!blockState.isIn(BlockTags.BEEHIVES) || blockState.get(BeehiveBlock.HONEY_LEVEL) < 5)
					return false
				env.world.playSound(null, position, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1f, 1f)
				BeehiveBlock.dropHoneycomb(env.world, position)
				(blockState.block as BeehiveBlock).takeHoney(env.world, blockState, position, null, BeehiveBlockEntity.BeeState.BEE_RELEASED)
				env.world.emitGameEvent(null, GameEvent.SHEAR, position)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("play_note"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (state.block !is NoteBlock)
					return false
				env.world.addSyncedBlockEvent(position, Blocks.NOTE_BLOCK, 0, 0)
				env.world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, position)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("ring_bell"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (state.block !is BellBlock)
					return false

				val facing = state.get(BellBlock.FACING)
				val ringDirection = when (state.get(BellBlock.ATTACHMENT)) {
					Attachment.SINGLE_WALL -> facing.rotateYClockwise()
					Attachment.DOUBLE_WALL -> facing.rotateYClockwise()
					else -> facing
				}
				(env.world.getBlockEntity(position) as BellBlockEntity).activate(ringDirection)
				env.world.playSound(null, position, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.0f)
				env.world.emitGameEvent(env.castingEntity, GameEvent.BLOCK_CHANGE, position)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("dispense"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (state.block !is DispenserBlock)
					return false
				(state.block as DispenserBlock).scheduledTick(state, env.world as ServerWorld, position, null)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("prime_tnt"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (state.block !is TntBlock)
					return false
				TntBlock.primeTnt(env.world, position)
				env.world.removeBlock(position, false)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("learn_akashic"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				if (env.castingEntity !is ServerPlayerEntity)
					return false
				val shelf = env.world.getBlockEntity(position)
				if (shelf !is BlockEntityAkashicBookshelf)
					return false

				val caster = env.castingEntity as ServerPlayerEntity
				val nbt = shelf.iotaTag ?: return false
				CastingUtils.giveIota(caster, IotaType.deserialize(nbt, caster.serverWorld))

				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("trigger_impetus"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				if (env.castingEntity !is ServerPlayerEntity)
					return false
				if (env.world.getBlockState(position).block !is BlockAbstractImpetus)
					return false
				(env.world.getBlockEntity(position) as BlockEntityAbstractImpetus).startExecution(env.castingEntity as ServerPlayerEntity)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("arm_armor_stands"), object : PrestidigitationHandler {
			override fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean {
				if (entity !is ArmorStandEntity)
					return false
				entity.setShowArms(!entity.shouldShowArms())
				entity.playSound(SoundEvents.ENTITY_ARMOR_STAND_PLACE, 1f, 1f)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("deprime_tnt"), object : PrestidigitationHandler {
			override fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean {
				if (entity !is TntEntity)
					return false
				if (entity.world.getBlockState(entity.blockPos).isReplaceable)
					entity.world.setBlockState(entity.blockPos, Blocks.TNT.defaultState)
				entity.discard()
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("shear"), object : PrestidigitationHandler {
			override fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean {
				if (entity !is Shearable)
					return false
				entity.sheared(SoundCategory.MASTER)
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("milk_squids"), object : PrestidigitationHandler {
			override fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean {
				if (entity !is SquidEntity)
					return false
				entity.squirt()
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("pandas_sneeze"), object : PrestidigitationHandler {
			override fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean {
				if (entity !is PandaEntity)
					return false
				entity.isSneezing = true
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("tease_creepers"), object : PrestidigitationHandler {
			override fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean {
				if (entity !is CreeperEntity)
					return false
				if (entity.isIgnited)
					entity.dataTracker.set(CreeperEntity.IGNITED, false)
				else
					entity.ignite()
				return true
			}
		})

		Registry.register(PRESTIDIGITATION_HANDLER, HexicalMain.id("puff_pufferfish"), object : PrestidigitationHandler {
			override fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean {
				if (entity !is PufferfishEntity)
					return false
				if (entity.puffState != 2) {
					entity.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP, 1f, 1f)
					entity.inflateTicks = 0
					entity.deflateTicks = 0
					entity.puffState = 2
				}
				return true
			}
		})

		PrestidigitationBlockBooleans.init()
		PrestidigitationBlockTransformations.init()
	}
}