package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class TriggerImpetusEffect : PrestidigitationEffect {
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		if (caster.world.getBlockState(position).block is BlockAbstractImpetus)
			((caster.world.getBlockEntity(position) ?: return) as BlockEntityAbstractImpetus).startExecution(caster)
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}