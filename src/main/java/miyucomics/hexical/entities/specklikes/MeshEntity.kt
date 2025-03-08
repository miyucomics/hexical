package miyucomics.hexical.entities.specklikes

import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtFloat
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class MeshEntity(entityType: EntityType<out MeshEntity>, world: World) : BaseSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.MESH_ENTITY, world)

	var clientVertices: MutableList<Vec3f> = mutableListOf()

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
		dataTracker.set(stateDataTracker, nbt.getCompound("shape"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
		nbt.putCompound("shape", dataTracker.get(stateDataTracker))
	}

	fun getShape(): List<Vec3Iota> {
		val list = dataTracker.get(stateDataTracker).getList("shape", NbtElement.FLOAT_TYPE.toInt())
		val deserializedVertices = mutableListOf<Vec3Iota>()
		for (i in 0 until (list.size / 3))
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
		this.dataTracker.set(stateDataTracker, compound)
	}

	override fun processState() {
		val list = this.dataTracker.get(stateDataTracker).getList("shape", NbtElement.FLOAT_TYPE.toInt())
		this.clientVertices = mutableListOf()
		for (i in 0 until (list.size / 3))
			clientVertices.add(Vec3f(list.getFloat(3 * i), list.getFloat(3 * i + 1), list.getFloat(3 * i + 2)))
	}
}