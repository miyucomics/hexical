package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class TransformBlockEffect(val state: BlockState) : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT

	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		caster.world.setBlockState(position, state)
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}