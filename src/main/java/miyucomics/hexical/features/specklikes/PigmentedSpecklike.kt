package miyucomics.hexical.features.specklikes

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

abstract class PigmentedSpecklike(entityType: EntityType<out PigmentedSpecklike>, world: World) : BaseSpecklike(entityType, world) {
	var clientPigment: FrozenPigment = FrozenPigment.DEFAULT.get()

	fun setPigment(pigment: FrozenPigment) = dataTracker.set(pigmentDataTracker, pigment.serializeToNBT())

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
		dataTracker.set(pigmentDataTracker, nbt.getCompound("pigment"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
		nbt.putCompound("pigment", dataTracker.get(pigmentDataTracker))
	}

	override fun initDataTracker() {
		super.initDataTracker()
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		if (data == pigmentDataTracker)
			this.clientPigment = FrozenPigment.fromNBT(dataTracker.get(pigmentDataTracker))
		super.onTrackedDataSet(data)
	}

	companion object {
		private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
	}
}