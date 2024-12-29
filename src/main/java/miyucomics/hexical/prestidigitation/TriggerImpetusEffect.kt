package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class TriggerImpetusEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {
		if (env.world.getBlockState(position).block is BlockAbstractImpetus) {
			val caster = env.castingEntity
			if (caster is ServerPlayerEntity)
				((env.world.getBlockEntity(position) ?: return) as BlockEntityAbstractImpetus).startExecution(caster)
		}
	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}