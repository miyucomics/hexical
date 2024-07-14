package miyucomics.hexical.entities

import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.entity.*
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.math.min

class SpikeEntity(entityType: EntityType<SpikeEntity>, world: World) : Entity(entityType, world) {
	private val animationLength = 10
	private val lingerPeriod = 10
	private var timer: Int = 0

	constructor(world: World, x: Double, y: Double, z: Double, direction: Direction, timer: Int): this(HexicalEntities.SPIKE_ENTITY, world) {
		this.setDirection(direction)
		this.setPosition(x, y, z)
		this.timer = timer
		this.dataTracker.set(timerDataTracker, timer)
	}

	override fun tick() {
		this.timer += 1
		if (this.timer == 0)
			for (livingEntity in world.getNonSpectatingEntities(LivingEntity::class.java, boundingBox))
				this.damage(livingEntity)
		if (this.timer > animationLength + lingerPeriod)
			this.discard()
	}

	private fun damage(target: LivingEntity) {
		if (!target.isAlive || target.isInvulnerable)
			return
		target.damage(DamageSource.MAGIC, 6.0f)
	}

	private fun setDirection(direction: Direction) {
		this.dataTracker.set(directionDataTracker, direction.id)
		if (direction.axis.isHorizontal) {
			this.pitch = 0.0f
			this.yaw = (direction.horizontal * 90).toFloat()
		} else {
			this.pitch = (-90 * direction.direction.offset()).toFloat()
			this.yaw = 0.0f
		}
		this.prevPitch = this.pitch
		this.prevYaw = this.yaw
	}

	fun getAnimationProgress(tickDelta: Float): Float {
		if (this.timer < -tickDelta)
			return 0f
		if (this.timer > -tickDelta && this.timer < this.animationLength)
			return min(1f, (this.timer + tickDelta) / this.animationLength.toFloat() + 0.05f)
		if (this.timer >= this.animationLength)
			return (this.animationLength - (this.timer + tickDelta - this.animationLength) / this.lingerPeriod.toFloat())
		return 1f
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		this.timer = nbt.getInt("timer")
		this.setDirection(Direction.byId(nbt.getInt("direction")))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putInt("timer", this.timer)
		nbt.putInt("direction", this.dataTracker.get(directionDataTracker))
	}

	fun getDirection(): Direction = Direction.byId(this.dataTracker.get(directionDataTracker))
	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0.5f
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)

	override fun initDataTracker() {
		this.dataTracker.startTracking(directionDataTracker, 0)
		this.dataTracker.startTracking(timerDataTracker, 0)
	}

	companion object {
		private val directionDataTracker: TrackedData<Int> = DataTracker.registerData(SpikeEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		private val timerDataTracker: TrackedData<Int> = DataTracker.registerData(SpikeEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
	}
}