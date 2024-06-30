package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.hasString
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.interfaces.Specklike
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.world.World

class SpeckEntity(entityType: EntityType<SpeckEntity?>?, world: World?) : Entity(entityType, world), Specklike {
	companion object {
		private val isPatternDataTracker: TrackedData<Boolean> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
		private val displayDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val rollDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
	}

	private var display = NbtCompound()
	private var isPattern = false
	private var pigment = NbtCompound()
	private var size = 1f
	private var thickness = 1f
	private var lifespan = -1
	private var roll = 0f

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
		dataTracker.startTracking(rollDataTracker, 0f)
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		display = nbt.getCompound("display")
		pigment = nbt.getCompound("pigment")
		roll = nbt.getFloat("roll")
		size = nbt.getFloat("size")
		thickness = nbt.getFloat("thickness")
		lifespan = nbt.getInt("lifespan")

		dataTracker.set(displayDataTracker, display)
		dataTracker.set(pigmentDataTracker, pigment)
		dataTracker.set(rollDataTracker, roll)
		dataTracker.set(sizeDataTracker, size)
		dataTracker.set(thicknessDataTracker, thickness)

		this.isPattern = !display.hasString("text")
		dataTracker.set(isPatternDataTracker, isPattern)
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("display", display)
		nbt.putCompound("pigment", pigment)
		nbt.putFloat("roll", roll)
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
			compound.putString("text", Text.Serializer.toJson(iota.display()))
			display = compound
		}
		dataTracker.set(displayDataTracker, display)
		dataTracker.set(isPatternDataTracker, isPattern)
	}

	override fun setLifespan(lifespan: Int) {
		this.lifespan = lifespan
	}

	override fun setPigment(pigment: FrozenColorizer) {
		this.pigment = pigment.serializeToNBT()
		dataTracker.set(pigmentDataTracker, this.pigment)
	}

	override fun setSize(size: Float) {
		this.size = size
		dataTracker.set(sizeDataTracker, size)
	}

	override fun setThickness(thickness: Float) {
		this.thickness = thickness
		dataTracker.set(thicknessDataTracker, thickness)
	}

	override fun setRoll(rotation: Float) {
		this.roll = rotation
		dataTracker.set(rollDataTracker, rotation)
	}

	override fun getSize(): Float = dataTracker.get(sizeDataTracker)
	override fun getThickness(): Float = dataTracker.get(thicknessDataTracker)
	override fun getRoll(): Float = dataTracker.get(rollDataTracker)
	override fun getPigment(): FrozenColorizer = FrozenColorizer.fromNBT(dataTracker.get(pigmentDataTracker))

	fun getText(): MutableText {
		return try {
			Text.Serializer.fromJson(dataTracker.get(displayDataTracker).getString("text"))!!
		} catch (exception: Exception) {
			Text.literal("This speck is broken.")
		}
	}
	fun getPattern(): HexPattern = HexPattern.fromNBT(dataTracker.get(displayDataTracker))
	fun getIsPattern(): Boolean = dataTracker.get(isPatternDataTracker)

	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)
}