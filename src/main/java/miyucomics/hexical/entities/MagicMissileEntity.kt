package miyucomics.hexical.entities

import miyucomics.hexical.HexicalMain
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class MagicMissileEntity(entityType: EntityType<out MagicMissileEntity?>?, world: World?) : PersistentProjectileEntity(entityType, world) {
	override fun getDamage() = 2.0
	override fun asItemStack() = null
	override fun hasNoGravity() = true
	override fun getDragInWater() = 1f
	override fun tryPickup(player: PlayerEntity) = false
	override fun getHitSound(): SoundEvent = SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK

	private fun shatter() {
		(world as ServerWorld).spawnParticles(ItemStackParticleEffect(ParticleTypes.ITEM, ItemStack(Items.AMETHYST_BLOCK, 1)), this.x, this.y, this.z, 8, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 10f)
		world.playSound(null, this.blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL, 0.5f, 1.5f)
		discard()
	}

	override fun tick() {
		super.tick()
		if (!world.isClient && (this.inGround || this.age > 200))
			shatter()
	}

	override fun onCollision(hitResult: HitResult) {
		super.onCollision(hitResult)
		if (hitResult.type != HitResult.Type.MISS && !world.isClient)
			shatter()
	}

	override fun onEntityHit(entityHitResult: EntityHitResult) {
		val target = entityHitResult.entity
		if (target.isInvulnerable)
			return
		if (target.type === EntityType.ENDERMAN)
			return

		val damageSource = if (owner == null) {
			DamageSource.arrow(this, this)
		} else {
			if (owner is LivingEntity)
				(owner as LivingEntity).onAttacking(target)
			DamageSource.arrow(this, owner)
		}

		if (target.damage(damageSource, damage.toFloat())) {
			if (target is LivingEntity) {
				val vector = velocity.multiply(1.0, 0.0, 1.0).normalize().multiply(0.6)
				if (vector.lengthSquared() > 0.0)
					target.addVelocity(vector.x, 0.1, vector.z)
				this.onHit(target)
			}
		} else {
			this.velocity = velocity.multiply(-1.0)
			this.yaw += 180.0f
			this.prevYaw += 180.0f
		}
	}
}