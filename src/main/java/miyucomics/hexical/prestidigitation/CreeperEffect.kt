package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class CreeperEffect : PrestidigitationEffect {
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {
		if (entity is CreeperEntity) {
			if (entity.isIgnited)
				entity.dataTracker.set(CreeperEntity.IGNITED, false)
			else
				entity.ignite()
		}
	}
}