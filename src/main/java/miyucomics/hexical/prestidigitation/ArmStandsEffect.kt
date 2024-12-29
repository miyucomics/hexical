package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos

class ArmStandsEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {
		if (entity is ArmorStandEntity) {
			entity.setShowArms(!entity.shouldShowArms())
			entity.playSound(SoundEvents.ENTITY_ARMOR_STAND_PLACE, 1f, 1f)
		}
	}
}