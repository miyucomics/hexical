package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.Blocks
import net.minecraft.block.NoteBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.event.GameEvent

class NoteblockEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {
		val state = env.world.getBlockState(position)
		if (state.block is NoteBlock) {
			env.world.addSyncedBlockEvent(position, Blocks.NOTE_BLOCK, 0, 0)
			env.world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, position)
		}
	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}