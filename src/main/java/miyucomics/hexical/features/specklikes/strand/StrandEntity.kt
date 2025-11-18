package miyucomics.hexical.features.specklikes.strand

import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.utils.asFloat
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.features.specklikes.FigureSpecklike
import miyucomics.hexical.inits.HexicalEntities
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtFloat
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class StrandEntity(entityType: EntityType<out StrandEntity>, world: World) : FigureSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.STRAND_ENTITY, world)

	var clientVertices: MutableList<Vec2f> = mutableListOf()

	fun getShape(): List<Vec3Iota> {
		val list = dataTracker.get(shapeDataTracker).getList("shape", NbtElement.FLOAT_TYPE.toInt()).iterator()
		val deserializedVertices = mutableListOf<Vec3Iota>()
		while (list.hasNext())
			deserializedVertices.add(Vec3Iota(Vec3d(list.next().asFloat.toDouble(), list.next().asFloat.toDouble(), 0.0)))
		return deserializedVertices
	}

	fun setShape(shape: List<Vec2f>) {
		val compound = NbtCompound()
		val list = NbtList()
		for (vertex in shape) {
			list.add(NbtFloat.of(vertex.x))
			list.add(NbtFloat.of(vertex.y))
		}
		compound.putList("shape", list)
		this.dataTracker.set(shapeDataTracker, compound)
	}

	override fun processShape(shape: NbtCompound) {
		this.clientVertices.clear()
		val list = shape.getList("shape", NbtElement.FLOAT_TYPE.toInt()).iterator()
		while (list.hasNext())
			clientVertices.add(Vec2f(list.next().asFloat, list.next().asFloat))
	}
}