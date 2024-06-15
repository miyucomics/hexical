package miyucomics.hexical.prestidigitation

import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.util.math.Vec3d

class ArmorStandEffect : PrestidigitationEffect {
	override fun effectBlock(position: Vec3d) {}
	override fun effectEntity(entity: Entity) {
		if (entity is ArmorStandEntity) {
			entity.kill()
		}
	}
}