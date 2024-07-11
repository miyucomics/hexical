package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class SquirtSquidsEffect : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT

	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {
		if (entity is SquidEntity)
			entity.squirt()
	}
}