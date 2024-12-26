package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class PistonEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {

	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}