package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World

private val patternDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
private val labelDataTracker: TrackedData<String> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.STRING)
private val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)

class SpeckEntity(entityType: EntityType<SpeckEntity?>?, world: World?) : Entity(entityType, world) {
	private var pattern: HexPattern = HexPattern.fromAngles("w", HexDir.WEST)
	private var pigment: FrozenColorizer = FrozenColorizer.DEFAULT.get()
	private var label: String = ""
	private var size: Float = 1f
	private var thickness: Float = 1f
	private var lifespan: Int = -1

	override fun tick() {
		lifespan--
		if (lifespan == 0)
			discard()
		super.tick()
	}

	override fun initDataTracker() {
		dataTracker.startTracking(patternDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
		dataTracker.startTracking(labelDataTracker, "")
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		pattern = HexPattern.fromNBT(nbt.getCompound("pattern"))
		pigment = FrozenColorizer.fromNBT(nbt.getCompound("pigment"))
		label = nbt.getString("label")
		size = nbt.getFloat("size")
		thickness = nbt.getFloat("thickness")
		lifespan = nbt.getInt("lifespan")
		dataTracker.set(patternDataTracker, nbt.getCompound("pattern"))
		dataTracker.set(pigmentDataTracker, nbt.getCompound("pigment"))
		dataTracker.set(labelDataTracker, label)
		dataTracker.set(sizeDataTracker, size)
		dataTracker.set(thicknessDataTracker, thickness)
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("pattern", pattern.serializeToNBT())
		nbt.putCompound("pigment", pigment.serializeToNBT())
		nbt.putString("label", label)
		nbt.putFloat("size", size)
		nbt.putFloat("thickness", thickness)
		nbt.putInt("lifespan", lifespan)
	}

	fun setPattern(pattern: HexPattern) {
		this.label = ""
		this.pattern = pattern
		dataTracker.set(labelDataTracker, label)
		dataTracker.set(patternDataTracker, pattern.serializeToNBT())
	}

	fun setLifespan(lifespan: Int) {
		this.lifespan = lifespan
	}

	fun setPigment(pigment: FrozenColorizer) {
		this.pigment = pigment
		dataTracker.set(pigmentDataTracker, pigment.serializeToNBT())
	}

	fun setLabel(label: String) {
		this.label = label
		dataTracker.set(labelDataTracker, label)
	}

	fun setSize(size: Float) {
		this.size = size
		dataTracker.set(sizeDataTracker, size)
	}

	fun setThickness(thickness: Float) {
		this.thickness = thickness
		dataTracker.set(thicknessDataTracker, thickness)
	}

	fun getPattern(): HexPattern = HexPattern.fromNBT(dataTracker.get(patternDataTracker))
	fun getPigment(): FrozenColorizer = FrozenColorizer.fromNBT(dataTracker.get(pigmentDataTracker))
	fun getLabel(): String = dataTracker.get(labelDataTracker)
	fun getSize(): Float = dataTracker.get(sizeDataTracker)
	fun getThickness(): Float = dataTracker.get(thicknessDataTracker)
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)
}