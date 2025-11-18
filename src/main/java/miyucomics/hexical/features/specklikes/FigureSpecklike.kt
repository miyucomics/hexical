package miyucomics.hexical.features.specklikes

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

// A subclass of specks that is meant for drawing figures, comes with utilities like thickness, storing and automatic compatibility with pigments and the pigment splashing pattern
// Generally if your speck does not render text or blocks or entities and just renders lines, polygons, etc... there is a good chance this is the thing for it
// Also comes with an NBT compound data tracker for storing information and a utility method to update client-sided caches
abstract class FigureSpecklike(entityType: EntityType<out FigureSpecklike>, world: World) : BaseSpecklike(entityType, world) {
	var clientPigment: FrozenPigment = FrozenPigment.DEFAULT.get()
	var clientThickness = 1f

	fun setPigment(pigment: FrozenPigment) = dataTracker.set(pigmentDataTracker, pigment.serializeToNBT())

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
		dataTracker.set(pigmentDataTracker, nbt.getCompound("pigment"))
		dataTracker.set(thicknessDataTracker, nbt.getFloat("thickness"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
		nbt.putCompound("pigment", dataTracker.get(pigmentDataTracker))
		nbt.putFloat("thickness", dataTracker.get(thicknessDataTracker))
	}

	override fun initDataTracker() {
		super.initDataTracker()
		dataTracker.startTracking(shapeDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		when (data) {
			shapeDataTracker -> processShape(dataTracker.get(shapeDataTracker))
			pigmentDataTracker -> this.clientPigment = FrozenPigment.fromNBT(dataTracker.get(pigmentDataTracker))
			thicknessDataTracker -> this.clientThickness = dataTracker.get(thicknessDataTracker)
			else -> super.onTrackedDataSet(data)
		}
	}

	abstract fun processShape(shape: NbtCompound)

	companion object {
		val shapeDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.FLOAT)
	}
}