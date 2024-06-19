package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.BellBlock
import net.minecraft.block.entity.BellBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.event.GameEvent

class RingBellEffect : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
		if (state.block is BellBlock) {
			val bell: BellBlockEntity = (caster.world.getBlockEntity(position)?: return) as BellBlockEntity
			bell.activate(state.get(BellBlock.FACING))
			caster.world.playSound(null, position, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.0f)
			caster.world.emitGameEvent(caster, GameEvent.BLOCK_CHANGE, position)
		}
	}
	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}