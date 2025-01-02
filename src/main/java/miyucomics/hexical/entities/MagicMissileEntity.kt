package miyucomics.hexical.entities

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalDamageTypes
import miyucomics.hexical.inits.HexicalEntities
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World


class MagicMissileEntity(entityType: EntityType<out MagicMissileEntity?>, world: World) : PersistentProjectileEntity(entityType, world) {
	constructor(world: World) : this(HexicalEntities.MAGIC_MISSILE_ENTITY, world)

	override fun handleStatus(status: Byte) {
		if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
			world.playSound(null, this.blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL, 0.25f, 1.5f)
			for (i in 0..7)
				world.addParticle(ItemStackParticleEffect(ParticleTypes.ITEM, ItemStack(Items.AMETHYST_BLOCK, 1)), this.x, this.y, this.z, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f)
		}
	}

	override fun tick() {
		super.tick()
		if (!world.isClient && (this.inGround || this.age > 200))
			shatter()
	}

	override fun onBlockHit(blockHitResult: BlockHitResult) {
		shatter()
	}

	override fun onEntityHit(entityHitResult: EntityHitResult) {
		val target = entityHitResult.entity
		target.damage(world.damageSources.create(HexicalDamageTypes.MAGIC_MISSILE, this, this.owner), 2f)
		target.velocity = this.velocity.multiply(1.0, 0.0, 1.0).normalize().multiply(0.6).add(0.0, 0.1, 0.0)
		shatter()
	}

	private fun shatter() {
		world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES)
		discard()
	}

	override fun asItemStack() = null
	override fun hasNoGravity() = true
	override fun getDragInWater() = 1f
	override fun tryPickup(player: PlayerEntity) = false
	override fun getHitSound(): SoundEvent = SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK
}