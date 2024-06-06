package miyucomics.hexical.entities

import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World

class MagicMissileEntity(entityType: EntityType<out MagicMissileEntity?>?, world: World?) : PersistentProjectileEntity(entityType, world) {
	override fun getPunch() = 0
	override fun getDamage() = 0.1
	override fun asItemStack() = null
	override fun hasNoGravity() = true
	override fun tryPickup(player: PlayerEntity) = false
	override fun getHitSound(): SoundEvent = SoundEvents.ENTITY_PLAYER_ATTACK_WEAK

	override fun tick() {
		super.tick()
		if (!world.isClient && (this.inGround || this.age > 200)) {
			discard()
		}
	}
}