package miyucomics.hexical.interfaces

import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

interface PrestidigitationEffect {
	fun effectBlock(position: Vec3d)
	fun effectEntity(entity: Entity)
}