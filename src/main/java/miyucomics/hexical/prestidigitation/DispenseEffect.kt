package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.DispenserBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class DispenseEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {
		val state = env.world.getBlockState(position)
		if (state.block is DispenserBlock)
			(state.block as DispenserBlock).scheduledTick(state, env.world as ServerWorld, position, null)
	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}