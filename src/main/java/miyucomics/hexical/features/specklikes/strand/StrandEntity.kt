package miyucomics.hexical.features.specklikes.strand

import miyucomics.hexical.features.specklikes.BaseSpecklike
import miyucomics.hexical.inits.HexicalEntities
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

class StrandEntity(entityType: EntityType<out StrandEntity>, world: World) : BaseSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.STRAND_ENTITY, world)

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
	}

	override fun initDataTracker() {
		super.initDataTracker()
	}
}