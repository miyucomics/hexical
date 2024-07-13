package miyucomics.hexical.prestidigitation

import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class PistonEffect : PrestidigitationEffect {
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}