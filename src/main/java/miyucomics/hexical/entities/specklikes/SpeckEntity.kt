package miyucomics.hexical.entities.specklikes

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.inits.HexicalEntities
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f
import net.minecraft.world.World

open class SpeckEntity(entityType: EntityType<out SpeckEntity>, world: World) : BaseSpecklike(entityType, world) {
	constructor(world: World) : this(HexicalEntities.SPECK_ENTITY, world)

	var clientIsText = false
	var clientText: Text = Text.empty()
	var clientVerts: List<Vec2f> = listOf()

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		super.readCustomDataFromNbt(nbt)
		dataTracker.set(displayDataTracker, nbt.getCompound("display"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		super.writeCustomDataToNbt(nbt)
		nbt.putCompound("display", dataTracker.get(displayDataTracker))
	}

	fun setIota(iota: Iota) {
		if (iota is PatternIota) {
			dataTracker.set(displayDataTracker, iota.pattern.serializeToNBT())
		} else {
			val compound = NbtCompound()
			val text = iota.display()
			compound.putString("text", Text.Serializer.toJson(Text.of(text.string.removePrefix("\"").removeSuffix("\"")).getWithStyle(text.style)[0]))
			dataTracker.set(displayDataTracker, compound)
		}
	}

	override fun initDataTracker() {
		super.initDataTracker()
		dataTracker.startTracking(displayDataTracker, NbtCompound())
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		super.onTrackedDataSet(data)
		if (data == displayDataTracker) {
			val raw = dataTracker.get(displayDataTracker)
			if (raw.contains("text")) {
				this.clientIsText = true
				this.clientText = Text.Serializer.fromJson(raw.getString("text"))!!
			} else {
				this.clientIsText = false
				this.clientVerts = RenderUtils.getNormalizedStrokes(HexPattern.fromNBT(raw))
			}
		}
	}

	companion object {
		private val displayDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
	}
}