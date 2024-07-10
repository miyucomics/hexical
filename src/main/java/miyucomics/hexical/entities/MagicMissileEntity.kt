package miyucomics.hexical.entities

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.registry.HexicalDamageTypes
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
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
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

class MagicMissileEntity(entityType: EntityType<out MagicMissileEntity?>, world: World) : PersistentProjectileEntity(entityType, world) {
	constructor(world: World) : this(HexicalEntities.MAGIC_MISSILE_ENTITY, world)

	override fun tick() {
		super.tick()
		if (!world.isClient && (this.inGround || this.age > 200))
			shatter()
	}

	override fun onBlockHit(blockHitResult: BlockHitResult) {
		shatter()
		super.onBlockHit(blockHitResult)
	}

	override fun onEntityHit(entityHitResult: EntityHitResult) {
		shatter()
		super.onEntityHit(entityHitResult)
	}

	override fun onHit(target: LivingEntity) {
		target.damage(HexicalDamageTypes.magicMissile(this, this.owner), 2f)
		val vector = velocity.multiply(1.0, 0.0, 1.0).normalize().multiply(0.6)
		if (vector.lengthSquared() > 0.0)
			target.addVelocity(vector.x, 0.1, vector.z)
		super.onHit(target)
	}

	private fun shatter() {
		(world as ServerWorld).spawnParticles(ItemStackParticleEffect(ParticleTypes.ITEM, ItemStack(Items.AMETHYST_BLOCK, 1)), this.x, this.y, this.z, 8, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 10f)
		world.playSound(null, this.blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL, 0.25f, 1.5f)
		discard()
	}

	override fun asItemStack() = null
	override fun hasNoGravity() = true
	override fun getDragInWater() = 1f
	override fun tryPickup(player: PlayerEntity) = false
	override fun getHitSound(): SoundEvent = SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK
}