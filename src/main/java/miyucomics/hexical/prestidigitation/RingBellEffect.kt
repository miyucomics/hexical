package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.BellBlock
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class RingBellEffect : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
		if (state.block is BellBlock)
			(state.block as BellBlock).ring(caster.world, position, Direction.UP)
	}
	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}