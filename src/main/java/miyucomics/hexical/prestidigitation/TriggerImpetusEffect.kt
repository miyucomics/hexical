package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import at.petrak.hexcasting.api.block.circle.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class TriggerImpetusEffect : PrestidigitationEffect {
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
		if (state.block is BlockAbstractImpetus) {
			val impetus: BlockEntityAbstractImpetus = (caster.world.getBlockEntity(position) ?: return) as BlockEntityAbstractImpetus
			impetus.activateSpellCircle(caster)
		}
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}