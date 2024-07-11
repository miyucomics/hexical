package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.DispenserBlock
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class DispenseEffect : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT

	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
		if (state.block is DispenserBlock)
			(state.block as DispenserBlock).scheduledTick(state, caster.world as ServerWorld, position, null)
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}