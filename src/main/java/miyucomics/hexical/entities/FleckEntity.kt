package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.interfaces.Specklike
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.Vec2f
import net.minecraft.world.World

class FleckEntity(entityType: EntityType<FleckEntity?>?, world: World?) : Entity(entityType, world), Specklike {
	companion object {
		private val shapeDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(FleckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(FleckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val rollDataTracker: TrackedData<Float> = DataTracker.registerData(FleckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(FleckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(FleckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
	}

	private var shape = NbtCompound()
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
		dataTracker.startTracking(shapeDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
		dataTracker.startTracking(rollDataTracker, 0f)
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		shape = nbt.getCompound("shape")
		pigment = nbt.getCompound("pigment")
		size = nbt.getFloat("size")
		thickness = nbt.getFloat("thickness")
		lifespan = nbt.getInt("lifespan")
		roll = nbt.getFloat("roll")

		dataTracker.set(shapeDataTracker, shape)
		dataTracker.set(pigmentDataTracker, pigment)
		dataTracker.set(sizeDataTracker, size)
		dataTracker.set(thicknessDataTracker, thickness)
		dataTracker.set(rollDataTracker, roll)
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("shape", shape)
		nbt.putCompound("pigment", pigment)
		nbt.putFloat("roll", roll)
		nbt.putFloat("size", size)
		nbt.putFloat("thickness", thickness)
		nbt.putInt("lifespan", lifespan)
	}

	fun setShape(points: List<Vec2f>) {
		val serialized = NbtCompound()
		val list = NbtList()
		for (point in points) {
			val data = NbtCompound()
			data.putFloat("x", point.x)
			data.putFloat("y", point.y)
			list.add(data)
		}
		serialized.put("points", list)
		shape = serialized
	}

	fun getShape(): List<Vec2f> {
		val deserialized = mutableListOf<Vec2f>()
		val serialized = dataTracker.get(shapeDataTracker).getList("points", NbtElement.COMPOUND_TYPE.toInt())
		for (compound in serialized) {
			val nbt = compound as NbtCompound
			deserialized.add(Vec2f(nbt.getFloat("x"), nbt.getFloat("y")))
		}
		return deserialized
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

	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)
}