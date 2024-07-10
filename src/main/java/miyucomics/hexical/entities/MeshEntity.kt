package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.interfaces.Specklike
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtFloat
import net.minecraft.nbt.NbtList
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class MeshEntity(entityType: EntityType<MeshEntity>, world: World) : Entity(entityType, world), Specklike {
	private var lifespan = -1

	// client-only
	var clientVertices: MutableList<Vec3d> = mutableListOf()
	var clientPigment: FrozenColorizer = FrozenColorizer.DEFAULT.get()
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

	fun setShape(shape: List<Vec3d>) {
		val compound = NbtCompound()
		val list = NbtList()
		for (vertex in shape) {
			list.add(NbtFloat.of(vertex.x.toFloat()))
			list.add(NbtFloat.of(vertex.y.toFloat()))
			list.add(NbtFloat.of(vertex.z.toFloat()))
		}
		compound.putList("shape", list)
		this.dataTracker.set(shapeDataTracker, compound)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		dataTracker.set(shapeDataTracker, nbt.getCompound("shape"))
		dataTracker.set(pigmentDataTracker, nbt.getCompound("pigment"))
		dataTracker.set(rollDataTracker, nbt.getFloat("roll"))
		dataTracker.set(sizeDataTracker, nbt.getFloat("size"))
		dataTracker.set(thicknessDataTracker, nbt.getFloat("thickness"))
		this.lifespan = nbt.getInt("lifespan")
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("shape", dataTracker.get(shapeDataTracker))
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
	override fun setPigment(pigment: FrozenColorizer) = dataTracker.set(pigmentDataTracker, pigment.serializeToNBT())
	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0f
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)

	override fun initDataTracker() {
		dataTracker.startTracking(shapeDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
		dataTracker.startTracking(rollDataTracker, 0f)
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		when (data) {
			shapeDataTracker -> {
				val list = this.dataTracker.get(shapeDataTracker).getList("shape", NbtElement.FLOAT_TYPE.toInt())
				this.clientVertices = mutableListOf()
				for (i in 0..(list.size / 3))
					clientVertices.add(Vec3d(list.getFloat(i).toDouble(), list.getFloat(i + 1).toDouble(), list.getFloat(i + 2).toDouble()))
			}
			pigmentDataTracker -> this.clientPigment = FrozenColorizer.fromNBT(dataTracker.get(pigmentDataTracker))
			sizeDataTracker -> this.clientSize = dataTracker.get(sizeDataTracker)
			rollDataTracker -> this.clientRoll = dataTracker.get(rollDataTracker)
			thicknessDataTracker -> this.clientThickness = dataTracker.get(thicknessDataTracker)
			else -> {}
		}
	}

	companion object {
		private val shapeDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(MeshEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(MeshEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(MeshEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(MeshEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val rollDataTracker: TrackedData<Float> = DataTracker.registerData(MeshEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
	}
}