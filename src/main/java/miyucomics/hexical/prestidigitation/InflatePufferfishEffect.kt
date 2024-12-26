package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.PufferfishEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos

class InflatePufferfishEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {
		if (entity is PufferfishEntity && entity.puffState != 2) {
			entity.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP, 1f, 1f)
			entity.inflateTicks = 0
			entity.deflateTicks = 0
			entity.puffState = 2
		}
	}
}