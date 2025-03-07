package miyucomics.hexical.blocks

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.abs

class PillowBlockEntity(pos: BlockPos, state: BlockState?) : BlockEntity(HexicalBlocks.PILLOW_BLOCK_ENTITY, pos, state) {
	private var targetYaw: Float
	private var targetPitch: Float
	var currentYaw: Float
	var currentPitch: Float
	private var tickCountdown = 0

	init {
		val random = HexicalMain.RANDOM
		this.targetYaw = random.nextFloat() * 360f
		this.currentYaw = this.targetYaw
		this.targetPitch = random.nextFloat() * 60f - 30f
		this.currentPitch = this.targetPitch
	}

	fun tick(world: World, pos: BlockPos) {
		val speed = 2.5f

		if (abs((currentYaw - targetYaw).toDouble()) > speed) {
			currentYaw += if (currentYaw < targetYaw) speed else -speed
		} else {
			currentYaw = targetYaw
		}

		if (abs((currentPitch - targetPitch).toDouble()) > speed) {
			currentPitch += if (currentPitch < targetPitch) speed else -speed
		} else {
			currentPitch = targetPitch
		}

		if (--tickCountdown <= 0) {
			val random = HexicalMain.RANDOM
			targetYaw = random.nextFloat() * 360f
			targetPitch = random.nextFloat() * 60f - 30f
			tickCountdown = 100
		}
	}
}