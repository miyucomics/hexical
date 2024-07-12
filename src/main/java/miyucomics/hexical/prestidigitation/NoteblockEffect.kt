package miyucomics.hexical.prestidigitation

import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.Blocks
import net.minecraft.block.NoteBlock
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.event.GameEvent

class NoteblockEffect : PrestidigitationEffect {
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
		if (state.block is NoteBlock) {
			caster.world.addSyncedBlockEvent(position, Blocks.NOTE_BLOCK, 0, 0)
			caster.world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, position)
		}
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}