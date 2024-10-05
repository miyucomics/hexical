package miyucomics.hexical.entities.specklikes

import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.inits.HexicalEntities
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtFloat
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import net.minecraft.world.World

@OptIn(ExperimentalStdlibApi::class)
open class MeshEntity(entityType: EntityType<out MeshEntity>, world: World) : BaseSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.MESH_ENTITY, world)

	var clientVertices: MutableList<Vec3f> = mutableListOf()

	fun getShape(): List<Vec3Iota> {
		val list = dataTracker.get(shapeDataTracker).getList("shape", NbtElement.FLOAT_TYPE.toInt())
		val deserializedVertices = mutableListOf<Vec3Iota>()
		for (i in 0..<(list.size / 3))
			deserializedVertices.add(Vec3Iota(Vec3d(list.getFloat(3 * i).toDouble(), list.getFloat(3 * i + 1).toDouble(), list.getFloat(3 * i + 2).toDouble())))
		return deserializedVertices
	}

	fun setShape(shape: List<Vec3f>) {
		val compound = NbtCompound()
		val list = NbtList()
		for (vertex in shape) {
			list.add(NbtFloat.of(vertex.x))
			list.add(NbtFloat.of(vertex.y))
			list.add(NbtFloat.of(vertex.z))
		}
		compound.putList("shape", list)
		this.dataTracker.set(shapeDataTracker, compound)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
		dataTracker.set(shapeDataTracker, nbt.getCompound("shape"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
		nbt.putCompound("shape", dataTracker.get(shapeDataTracker))
	}

	override fun initDataTracker() {
		super.initDataTracker()
		dataTracker.startTracking(shapeDataTracker, NbtCompound())
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		super.onTrackedDataSet(data)
		if (data == shapeDataTracker) {
			val list = this.dataTracker.get(shapeDataTracker).getList("shape", NbtElement.FLOAT_TYPE.toInt())
			this.clientVertices = mutableListOf()
			for (i in 0..<(list.size / 3))
				clientVertices.add(Vec3f(list.getFloat(3 * i), list.getFloat(3 * i + 1), list.getFloat(3 * i + 2)))
		}
	}

	companion object {
		private val shapeDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(MeshEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
	}
}