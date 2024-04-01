package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

val isPatternDataTracker: TrackedData<Boolean> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
val displayDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)

class SpeckEntity(entityType: EntityType<SpeckEntity?>?, world: World?) : Entity(entityType, world) {
	private var display = NbtCompound()
	private var isPattern = false
	private var pigment = NbtCompound()
	private var size = 1f
	private var thickness = 1f
	private var lifespan = -1

	override fun tick() {
		if (lifespan != -1)
			lifespan--
		if (lifespan == 0)
			discard()
		super.tick()
	}

	override fun initDataTracker() {
		dataTracker.startTracking(isPatternDataTracker, false)
		dataTracker.startTracking(displayDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		display = nbt.getCompound("display")
		pigment = nbt.getCompound("pigment")
		size = nbt.getFloat("size")
		thickness = nbt.getFloat("thickness")
		lifespan = nbt.getInt("lifespan")
		dataTracker.set(displayDataTracker, display)
		dataTracker.set(isPatternDataTracker, isPattern)
		dataTracker.set(pigmentDataTracker, pigment)
		dataTracker.set(sizeDataTracker, size)
		dataTracker.set(thicknessDataTracker, thickness)

		this.isPattern = HexIotaTypes.deserialize(display, world as ServerWorld) is PatternIota
		dataTracker.set(isPatternDataTracker, isPattern)
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("display", display)
		nbt.putCompound("pigment", pigment)
		nbt.putFloat("size", size)
		nbt.putFloat("thickness", thickness)
		nbt.putInt("lifespan", lifespan)
	}

	fun setIota(iota: Iota) {
		this.isPattern = iota is PatternIota
		if (isPattern) {
			display = (iota as PatternIota).pattern.serializeToNBT()
		} else {
			val compound = NbtCompound()
			compound.putString("text", iota.display().string)
			display = compound
		}
		dataTracker.set(displayDataTracker, display)
		dataTracker.set(isPatternDataTracker, isPattern)
	}

	fun setLifespan(lifespan: Int) {
		this.lifespan = lifespan
	}

	fun setPigment(pigment: FrozenColorizer) {
		this.pigment = pigment.serializeToNBT()
		dataTracker.set(pigmentDataTracker, this.pigment)
	}

	fun setSize(size: Float) {
		this.size = size
		dataTracker.set(sizeDataTracker, size)
	}

	fun setThickness(thickness: Float) {
		this.thickness = thickness
		dataTracker.set(thicknessDataTracker, thickness)
	}

	fun getSize(): Float = dataTracker.get(sizeDataTracker)
	fun getThickness(): Float = dataTracker.get(thicknessDataTracker)
	fun getPigment(): FrozenColorizer = FrozenColorizer.fromNBT(dataTracker.get(pigmentDataTracker))

	fun getText(): String = dataTracker.get(displayDataTracker).getString("text")
	fun getPattern(): HexPattern = HexPattern.fromNBT(dataTracker.get(displayDataTracker))
	fun getIsPattern(): Boolean = dataTracker.get(isPatternDataTracker)

	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)
}