package miyucomics.hexical.features.prestidigitation.handlers

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandler
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandlerBlock
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.block.*
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.block.entity.BeehiveBlockEntity
import net.minecraft.block.entity.BellBlockEntity
import net.minecraft.block.enums.Attachment
import net.minecraft.block.enums.ComparatorMode
import net.minecraft.item.AxeItem
import net.minecraft.item.ShovelItem
import net.minecraft.registry.tag.BlockTags
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.event.GameEvent

object PrestidigitationHandlersBlock {
	fun init(register: (PrestidigitationHandler) -> Unit) {
		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.COMPARATOR)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.COMPARATOR_MODE, when (state.get(Properties.COMPARATOR_MODE)) {
					ComparatorMode.COMPARE -> ComparatorMode.SUBTRACT
					ComparatorMode.SUBTRACT -> ComparatorMode.COMPARE
				}))
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.PUMPKIN)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, Blocks.CARVED_PUMPKIN.defaultState.with(Properties.HORIZONTAL_FACING, Properties.HORIZONTAL_FACING.values.random()))
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = AxeItem.STRIPPED_BLOCKS.containsKey(getBlock(env, pos))
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, AxeItem.STRIPPED_BLOCKS[getBlock(env, pos)]!!.defaultState)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = ShovelItem.PATH_STATES.containsKey(getBlock(env, pos))
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, ShovelItem.PATH_STATES[getBlock(env, pos)]!!)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.BUTTONS)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				(state.block as ButtonBlock).powerOn(state, env.world, pos)
				(state.block as ButtonBlock).playClickSound(null, env.world, pos, true)
				env.world.emitGameEvent(null, GameEvent.BLOCK_ACTIVATE, pos)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.FIRE)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				env.world.removeBlock(pos, false)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS) && getBlockState(env, pos.up()).isAir
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos.up(), Blocks.SOUL_FIRE.defaultState)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.PRESSURE_PLATES)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.POWERED, !state.get(Properties.POWERED)))
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isIn(BlockTags.CAULDRONS)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, Blocks.CAULDRON.defaultState)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos): Boolean {
				val state = getBlockState(env, pos)
				return state.isIn(BlockTags.CANDLES) || state.isIn(BlockTags.CANDLE_CAKES) || state.isIn(BlockTags.CAMPFIRES)
			}

			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.LIT, !state.get(Properties.LIT)))
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos): Boolean {
				val state = getBlockState(env, pos)
				return state.isIn(BlockTags.DOORS) || state.isIn(BlockTags.TRAPDOORS) || state.isIn(BlockTags.FENCE_GATES)
			}

			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(Properties.OPEN, !state.get(Properties.OPEN)))
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos): Boolean {
				val state = getBlockState(env, pos)
				return state.isIn(BlockTags.BEEHIVES) && state.get(BeehiveBlock.HONEY_LEVEL) == 5
			}

			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				env.world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1f, 1f)
				BeehiveBlock.dropHoneycomb(env.world, pos)
				(state.block as BeehiveBlock).takeHoney(env.world, state, pos, env.castingEntity as? ServerPlayerEntity, BeehiveBlockEntity.BeeState.BEE_RELEASED)
				env.world.emitGameEvent(null, GameEvent.SHEAR, pos)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.NOTE_BLOCK)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				env.world.addSyncedBlockEvent(pos, Blocks.NOTE_BLOCK, 0, 0)
				env.world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, pos)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
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

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlock(env, pos) is DispenserBlock
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				(state.block as DispenserBlock).scheduledTick(state, env.world as ServerWorld, pos, null)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = getBlockState(env, pos).isOf(Blocks.TNT)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				TntBlock.primeTnt(env.world, pos)
				env.world.removeBlock(pos, false)
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = env.castingEntity is ServerPlayerEntity && getBlockState(env, pos).isOf(HexBlocks.AKASHIC_BOOKSHELF)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val caster = env.castingEntity as ServerPlayerEntity
				val nbt = (env.world.getBlockEntity(pos) as BlockEntityAkashicBookshelf).iotaTag ?: return
				CastingUtils.giveIota(caster, IotaType.deserialize(nbt, caster.serverWorld))
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = env.castingEntity is ServerPlayerEntity && getBlockState(env, pos).isIn(HexTags.Blocks.IMPETI)
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				(env.world.getBlockEntity(pos) as BlockEntityAbstractImpetus).startExecution(env.castingEntity as ServerPlayerEntity)
			}
		})

		register(PrestidigitationHandlerBlock.blockEntity(AbstractFurnaceBlockEntity::class.java) { world, pos, furnace ->
			furnace.burnTime += AbstractFurnaceBlockEntity.DEFAULT_COOK_TIME
			furnace.fuelTime = furnace.burnTime
			world.setBlockState(pos, world.getBlockState(pos).with(AbstractFurnaceBlock.LIT, true))
			furnace.markDirty()
		})
	}
}