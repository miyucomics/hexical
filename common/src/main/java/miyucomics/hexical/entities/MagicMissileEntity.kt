package miyucomics.hexical.entities

import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

class MagicMissileEntity(entityType: EntityType<MagicMissileEntity?>?, world: World?) : ProjectileEntity(entityType, world) {
	override fun onEntityHit(entityHitResult: EntityHitResult?) {
		entityHitResult!!.entity.addVelocity(0.0, 1.0, 0.0)
	}

	override fun initDataTracker() {}
}