package miyucomics.hexical.features.specklikes

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World

abstract class BaseSpecklike(entityType: EntityType<out BaseSpecklike>, world: World) : Entity(entityType, world) {
	private var lifespan = -1

	var clientSize = 1f
	var clientThickness = 1f
	var clientRoll = 0f

	override fun tick() {
		if (lifespan != -1)
			lifespan--
		if (lifespan == 0)
			discard()
		super.tick()
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		dataTracker.set(rollDataTracker, nbt.getFloat("roll"))
		dataTracker.set(sizeDataTracker, nbt.getFloat("size"))
		dataTracker.set(thicknessDataTracker, nbt.getFloat("thickness"))
		this.lifespan = nbt.getInt("lifespan")
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putFloat("roll", dataTracker.get(rollDataTracker))
		nbt.putFloat("size", dataTracker.get(sizeDataTracker))
		nbt.putFloat("thickness", dataTracker.get(thicknessDataTracker))
		nbt.putInt("lifespan", lifespan)
	}

	fun setLifespan(lifespan: Int) {
		this.lifespan = lifespan
	}

	fun setSize(size: Float) = dataTracker.set(sizeDataTracker, size)
	fun setRoll(rotation: Float) = dataTracker.set(rollDataTracker, rotation)
	fun setThickness(thickness: Float) = dataTracker.set(thicknessDataTracker, thickness)
	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0.25f
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)

	override fun initDataTracker() {
		dataTracker.startTracking(rollDataTracker, 0f)
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		when (data) {
			sizeDataTracker -> this.clientSize = dataTracker.get(sizeDataTracker)
			rollDataTracker -> this.clientRoll = dataTracker.get(rollDataTracker)
			thicknessDataTracker -> this.clientThickness = dataTracker.get(thicknessDataTracker)
		}
	}

	companion object {
		private val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val rollDataTracker: TrackedData<Float> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.FLOAT)
	}
}