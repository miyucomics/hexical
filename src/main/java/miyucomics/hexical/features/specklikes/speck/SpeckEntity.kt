package miyucomics.hexical.features.specklikes.speck

import miyucomics.hexical.features.specklikes.BaseSpecklike
import miyucomics.hexical.inits.HexicalEntities
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.world.World

class SpeckEntity(entityType: EntityType<out SpeckEntity>, world: World) : BaseSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.SPECK_ENTITY, world)

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
		dataTracker.set(textDataTracker, Text.Serializer.fromJson("text"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
		nbt.putString("text", Text.Serializer.toJson(dataTracker.get(textDataTracker)))
	}

	fun setText(text: Text) {
		dataTracker.set(textDataTracker, text)
	}

	override fun initDataTracker() {
		super.initDataTracker()
		dataTracker.startTracking(textDataTracker, Text.empty())
	}

	companion object {
		val textDataTracker: TrackedData<Text> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.TEXT_COMPONENT)
	}
}