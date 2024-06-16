package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos

class ArmStandsEffect : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT / 10
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {}
	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {
		if (entity is ArmorStandEntity) {
			entity.setShowArms(!entity.shouldShowArms())
			entity.playSound(SoundEvents.ENTITY_ARMOR_STAND_PLACE, 1f, 1f)
		}
	}
}