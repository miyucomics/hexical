package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class CollideEffect : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT

	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
		state.block.onEntityCollision(state, caster.world, position, caster)
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}