package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import net.minecraft.block.Blocks
import net.minecraft.block.entity.EndGatewayBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.particle.BlockDustParticle
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class MagicMissileEntity(entityType: EntityType<MagicMissileEntity?>?, world: World?) : ProjectileEntity(entityType, world) {
	private var pigment: FrozenColorizer = FrozenColorizer.DEFAULT.get()

	override fun tick() {
		val hitResult = ProjectileUtil.getCollision(this) { entity: Entity? -> this.canHit(entity) }
		var bl = false
		if (hitResult.type == HitResult.Type.BLOCK) {
			val blockPos = (hitResult as BlockHitResult).blockPos
			val blockState = world.getBlockState(blockPos)
			if (blockState.isOf(Blocks.NETHER_PORTAL)) {
				this.setInNetherPortal(blockPos)
				bl = true
			} else if (blockState.isOf(Blocks.END_GATEWAY)) {
				val blockEntity = world.getBlockEntity(blockPos)
				if (blockEntity is EndGatewayBlockEntity && EndGatewayBlockEntity.canTeleport(this))
					EndGatewayBlockEntity.tryTeleportingEntity(this.world, blockPos, blockState, this, blockEntity as EndGatewayBlockEntity?)
				bl = true
			}
		}
		if (hitResult.type != HitResult.Type.MISS && !bl)
			this.onCollision(hitResult)
		this.checkBlockCollision()
		val vec3d = this.velocity
		val d = this.x + vec3d.x
		val e = this.y + vec3d.y
		val f = this.z + vec3d.z
		setPos(d, e, f)
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound?) {
		super.writeCustomDataToNbt(nbt)
		nbt!!.put("pigment", pigment.serializeToNBT())
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound?) {
		super.readCustomDataFromNbt(nbt)
		pigment = FrozenColorizer.fromNBT(nbt!!.getCompound("pigment"))
	}

	fun setPigment(pigment: FrozenColorizer) {
		this.pigment = pigment
	}

	override fun onEntityHit(entityHitResult: EntityHitResult) {
		super.onEntityHit(entityHitResult)
		entityHitResult.entity.damage(DamageSource.thrownProjectile(this, this.owner), 1f)
	}

	override fun onCollision(hitResult: HitResult) {
		super.onCollision(hitResult)
		world.playSound(hitResult.pos.x, hitResult.pos.y, hitResult.pos.z, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.MASTER, 1f, 1f, true)
		if (world.isClient)
			MinecraftClient.getInstance().particleManager.addParticle(BlockDustParticle(world as ClientWorld, hitResult.pos.x, hitResult.pos.y, hitResult.pos.z, 0.0, 0.0, 0.0, Blocks.AMETHYST_BLOCK.defaultState))
		if (!world.isClient) {
			world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES)
			this.discard()
		}
	}

	override fun initDataTracker() {}
}