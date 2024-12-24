package miyucomics.hexical.entities.specklikes

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.interfaces.Specklike
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

abstract class BaseSpecklike(entityType: EntityType<out BaseSpecklike>, world: World) : Entity(entityType, world), Specklike {
	private var lifespan = -1

	var clientPigment: FrozenPigment = FrozenPigment.DEFAULT.get()
	var clientSize = 1f
	var clientThickness = 1f
	var clientRoll = 0f

	open fun processState() {}

	override fun tick() {
		if (lifespan != -1)
			lifespan--
		if (lifespan == 0)
			discard()
		super.tick()
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		dataTracker.set(pigmentDataTracker, nbt.getCompound("pigment"))
		dataTracker.set(rollDataTracker, nbt.getFloat("roll"))
		dataTracker.set(sizeDataTracker, nbt.getFloat("size"))
		dataTracker.set(thicknessDataTracker, nbt.getFloat("thickness"))
		this.lifespan = nbt.getInt("lifespan")
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("pigment", dataTracker.get(pigmentDataTracker))
		nbt.putFloat("roll", dataTracker.get(rollDataTracker))
		nbt.putFloat("size", dataTracker.get(sizeDataTracker))
		nbt.putFloat("thickness", dataTracker.get(thicknessDataTracker))
		nbt.putInt("lifespan", lifespan)
	}

	override fun setLifespan(lifespan: Int) {
		this.lifespan = lifespan
	}

	override fun setSize(size: Float) = dataTracker.set(sizeDataTracker, size)
	override fun setRoll(rotation: Float) = dataTracker.set(rollDataTracker, rotation)
	override fun setThickness(thickness: Float) = dataTracker.set(thicknessDataTracker, thickness)
	override fun setPigment(pigment: FrozenPigment) = dataTracker.set(pigmentDataTracker, pigment.serializeToNBT())
	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0.25f
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)

	override fun initDataTracker() {
		dataTracker.startTracking(stateDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
		dataTracker.startTracking(rollDataTracker, 0f)
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		when (data) {
			stateDataTracker -> processState()
			pigmentDataTracker -> this.clientPigment = FrozenPigment.fromNBT(dataTracker.get(pigmentDataTracker))
			sizeDataTracker -> this.clientSize = dataTracker.get(sizeDataTracker)
			rollDataTracker -> this.clientRoll = dataTracker.get(rollDataTracker)
			thicknessDataTracker -> this.clientThickness = dataTracker.get(thicknessDataTracker)
			else -> {}
		}
	}

	companion object {
		val stateDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val rollDataTracker: TrackedData<Float> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.FLOAT)
	}
}