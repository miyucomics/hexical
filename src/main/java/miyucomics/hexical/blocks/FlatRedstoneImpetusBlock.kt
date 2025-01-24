package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.common.lib.HexSounds
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class FlatRedstoneImpetusBlock : FlatImpetusBlock() {
	override fun createBlockEntity(pos: BlockPos, state: BlockState) = FlatRedstoneImpetusBlockEntity(pos, state)

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		super.appendProperties(builder)
		builder.add(POWERED)
	}

	override fun onUse(state: BlockState, world: World, position: BlockPos, pPlayer: PlayerEntity, pHand: Hand, pHit: BlockHitResult): ActionResult {
		val tile = world.getBlockEntity(position)
		if (world is ServerWorld && tile is FlatRedstoneImpetusBlockEntity) {
			val usedStack = pPlayer.getStackInHand(pHand)
			if (usedStack.isEmpty && pPlayer.isSneaky) {
				tile.clearPlayer()
				tile.sync()
				world.playSound(null, position, HexSounds.IMPETUS_REDSTONE_CLEAR, SoundCategory.BLOCKS, 1f, 1f)
				return ActionResult.success(world.isClient)
			}

			val datumContainer = IXplatAbstractions.INSTANCE.findDataHolder(usedStack)
			if (datumContainer != null) {
				val stored = datumContainer.readIota(world)
				if (stored is EntityIota) {
					val entity = stored.entity
					if (entity is PlayerEntity) {
						tile.setPlayer(entity.gameProfile, entity.getUuid())
						tile.sync()
						world.playSound(null, position, HexSounds.IMPETUS_REDSTONE_DING, SoundCategory.BLOCKS, 1f, 1f)
						return ActionResult.success(world.isClient)
					}
				}
			}
		}

		return ActionResult.PASS
	}

	override fun scheduledTick(state: BlockState, world: ServerWorld, position: BlockPos, pRandom: Random) {
		super.scheduledTick(state, world, position, pRandom)
		val tile = world.getBlockEntity(position)
		if (tile is FlatRedstoneImpetusBlockEntity)
			tile.updatePlayerProfile()
	}

	override fun neighborUpdate(state: BlockState, world: World, position: BlockPos, pBlock: Block, pFromPos: BlockPos, pIsMoving: Boolean) {
		super.neighborUpdate(state, world, position, pBlock, pFromPos, pIsMoving)
		if (world is ServerWorld) {
			val prevPowered = state.get(POWERED)
			val isPowered = world.isReceivingRedstonePower(position)
			if (prevPowered != isPowered) {
				world.setBlockState(position, state.with(POWERED, isPowered))
				val tile = world.getBlockEntity(position)
				if (isPowered && tile is FlatRedstoneImpetusBlockEntity)
					tile.startExecution(tile.getStoredPlayer())
			}
		}
	}

	companion object {
		val POWERED: BooleanProperty = Properties.POWERED
	}
}