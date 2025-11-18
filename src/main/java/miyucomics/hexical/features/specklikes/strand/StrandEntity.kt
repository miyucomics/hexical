package miyucomics.hexical.features.specklikes.strand

import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.features.specklikes.BaseSpecklike
import miyucomics.hexical.features.specklikes.FigureSpecklike
import miyucomics.hexical.inits.HexicalEntities
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec2f
import net.minecraft.world.World

class StrandEntity(entityType: EntityType<out StrandEntity>, world: World) : FigureSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.STRAND_ENTITY, world)

	var clientVertices: MutableList<Vec2f> = mutableListOf()

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

	companion object {
		private val shapeDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(BaseSpecklike::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
	}
}