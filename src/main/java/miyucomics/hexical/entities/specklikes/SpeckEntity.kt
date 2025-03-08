package miyucomics.hexical.entities.specklikes

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.registry.HexicalEntities
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f
import net.minecraft.world.World

class SpeckEntity(entityType: EntityType<out SpeckEntity>, world: World) : BaseSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.SPECK_ENTITY, world)

	var clientIsText = false
	var clientText: Text = Text.empty()
	var clientVerts: List<Vec2f> = listOf()

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
		dataTracker.set(stateDataTracker, nbt.getCompound("display"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
		nbt.putCompound("display", dataTracker.get(stateDataTracker))
	}

	fun setIota(iota: Iota) {
		if (iota is PatternIota) {
			dataTracker.set(stateDataTracker, iota.pattern.serializeToNBT())
		} else {
			val compound = NbtCompound()
			compound.putString("text", Text.Serializer.toJson(iota.display()))
			dataTracker.set(stateDataTracker, compound)
		}
	}

	override fun processState() {
		val raw = dataTracker.get(stateDataTracker)
		if (raw.contains("text")) {
			this.clientIsText = true
			this.clientText = Text.Serializer.fromJson(raw.getString("text"))!!
		} else {
			this.clientIsText = false
			this.clientVerts = RenderUtils.getNormalizedStrokes(HexPattern.fromNBT(raw))
		}
	}
}