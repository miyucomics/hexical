package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.BellBlock
import net.minecraft.block.entity.BellBlockEntity
import net.minecraft.block.enums.Attachment
import net.minecraft.entity.Entity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.event.GameEvent

class RingBellEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {
		val state = env.world.getBlockState(position)
		if (state.block is BellBlock) {
			val bell = (env.world.getBlockEntity(position) ?: return) as BellBlockEntity
			val facing = state.get(BellBlock.FACING)
			val ringDirection = when (state.get(BellBlock.ATTACHMENT)) {
				Attachment.SINGLE_WALL -> facing.rotateYClockwise()
				Attachment.DOUBLE_WALL -> facing.rotateYClockwise()
				else -> facing
			}
			bell.activate(ringDirection)
			env.world.playSound(null, position, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.0f)
			env.world.emitGameEvent(env.castingEntity, GameEvent.BLOCK_CHANGE, position)
		}
	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}