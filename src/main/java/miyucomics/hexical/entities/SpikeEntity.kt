package miyucomics.hexical.entities

import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class SpikeEntity(entityType: EntityType<SpikeEntity>, world: World) : Entity(entityType, world) {
	private var timer: Int = 0

	constructor(world: World, x: Double, y: Double, z: Double, direction: Direction, timer: Int): this(HexicalEntities.SPIKE_ENTITY, world) {
		this.setDirection(direction)
		this.setPosition(x, y, z)
		this.timer = timer
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
	}

	companion object {
		private val directionDataTracker: TrackedData<Int> = DataTracker.registerData(SpikeEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
	}
}