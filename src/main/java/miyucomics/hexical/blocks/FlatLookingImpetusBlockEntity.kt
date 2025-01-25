package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.common.lib.HexSounds
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.enums.WallMountLocation
import net.minecraft.entity.EquipmentSlot
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

class FlatLookingImpetusBlockEntity(position: BlockPos, state: BlockState) : BlockEntityAbstractImpetus(HexicalBlocks.FLAT_LOOKING_IMPETUS_BLOCK_ENTITY, position, state) {
	private var lookAmount = 0

	override fun getStartDirection(): Direction {
		if (this.cachedState.get(FlatImpetusBlock.ATTACH_FACE) == WallMountLocation.WALL)
			return Direction.DOWN
		return super.getStartDirection()
	}

	override fun saveModData(tag: NbtCompound) {
		super.saveModData(tag)
		tag.putInt(TAG_LOOK_AMOUNT, this.lookAmount)
	}

	override fun loadModData(tag: NbtCompound) {
		super.loadModData(tag)
		this.lookAmount = tag.getInt(TAG_LOOK_AMOUNT)
	}

	companion object {
		private const val MAX_LOOK_AMOUNT: Int = 30
		private const val TAG_LOOK_AMOUNT: String = "look_amount"

		fun serverTick(world: World, pos: BlockPos, bs: BlockState, self: FlatLookingImpetusBlockEntity) {
			if (bs.get(BlockCircleComponent.ENERGIZED))
				return

			val range = 20
			var looker: ServerPlayerEntity? = null
			for (player in world.getNonSpectatingEntities(ServerPlayerEntity::class.java, Box(pos.add(-range, -range, -range), pos.add(range, range, range)))) {
				val hat = player.getEquippedStack(EquipmentSlot.HEAD)
				if (!hat.isEmpty && hat.isOf(Blocks.CARVED_PUMPKIN.asItem()))
					continue

				val lookEnd = player.rotationVector.multiply((range / 1.5f).toDouble())
				val hit = world.raycast(RaycastContext(player.eyePos, player.eyePos.add(lookEnd), RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, player))
				if (hit.type == HitResult.Type.BLOCK && hit.blockPos == pos) {
					looker = player
					break
				}
			}

			val newLook = MathHelper.clamp(self.lookAmount + (if (looker == null) -1 else 1), 0, MAX_LOOK_AMOUNT)
			if (newLook == MAX_LOOK_AMOUNT) {
				self.lookAmount = 0
				self.startExecution(looker)
			} else {
				if (newLook % 5 == 1) {
					val progress = newLook.toFloat() / MAX_LOOK_AMOUNT
					val pitch = MathHelper.lerp(progress, 0.5f, 1.2f)
					val volume = MathHelper.lerp(progress, 0.2f, 1.2f)
					world.playSound(null, pos, HexSounds.IMPETUS_LOOK_TICK, SoundCategory.BLOCKS, volume, pitch)
				}
				self.lookAmount = newLook
				self.markDirty()
			}
		}
	}
}