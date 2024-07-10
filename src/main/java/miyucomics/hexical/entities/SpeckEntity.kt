package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.interfaces.Specklike
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f
import net.minecraft.world.World

class SpeckEntity(entityType: EntityType<SpeckEntity?>?, world: World?) : Entity(entityType, world), Specklike {
	companion object {
		private val displayDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		private val sizeDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val thicknessDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
		private val rollDataTracker: TrackedData<Float> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
	}

	private var lifespan = -1

	// client-only
	var clientIsText = false
	var clientText: Text = Text.empty()
	var clientVerts: List<Vec2f> = listOf()
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

	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0.5f

	override fun initDataTracker() {
		dataTracker.startTracking(displayDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
		dataTracker.startTracking(rollDataTracker, 0f)
		dataTracker.startTracking(sizeDataTracker, 1f)
		dataTracker.startTracking(thicknessDataTracker, 1f)
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		when (data) {
			displayDataTracker -> {
				val raw = dataTracker.get(displayDataTracker)
				if (raw.contains("text")) {
					this.clientIsText = true
					this.clientText = Text.Serializer.fromJson(raw.getString("text"))!!
				} else {
					this.clientIsText = false
					this.clientVerts = RenderUtils.getNormalizedStrokes(HexPattern.fromNBT(raw))
				}
			}
			pigmentDataTracker -> this.clientPigment = FrozenColorizer.fromNBT(dataTracker.get(pigmentDataTracker))
			sizeDataTracker -> this.clientSize = dataTracker.get(sizeDataTracker)
			rollDataTracker -> this.clientRoll = dataTracker.get(rollDataTracker)
			thicknessDataTracker -> this.clientThickness = dataTracker.get(thicknessDataTracker)
			else -> {}
		}
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		dataTracker.set(displayDataTracker, nbt.getCompound("display"))
		dataTracker.set(pigmentDataTracker, nbt.getCompound("pigment"))
		dataTracker.set(rollDataTracker, nbt.getFloat("roll"))
		dataTracker.set(sizeDataTracker, nbt.getFloat("size"))
		dataTracker.set(thicknessDataTracker, nbt.getFloat("thickness"))
		this.lifespan = nbt.getInt("lifespan")
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("display", dataTracker.get(displayDataTracker))
		nbt.putCompound("pigment", dataTracker.get(pigmentDataTracker))
		nbt.putFloat("roll", dataTracker.get(rollDataTracker))
		nbt.putFloat("size", dataTracker.get(sizeDataTracker))
		nbt.putFloat("thickness", dataTracker.get(thicknessDataTracker))
		nbt.putInt("lifespan", lifespan)
	}

	fun setIota(iota: Iota) {
		if (iota is PatternIota) {
			dataTracker.set(displayDataTracker, iota.pattern.serializeToNBT())
		} else {
			val compound = NbtCompound()
			compound.putString("text", Text.Serializer.toJson(iota.display()))
			dataTracker.set(displayDataTracker, compound)
		}
	}

	override fun setLifespan(lifespan: Int) {
		this.lifespan = lifespan
	}

	override fun setSize(size: Float) = dataTracker.set(sizeDataTracker, size)
	override fun setRoll(rotation: Float) = dataTracker.set(rollDataTracker, rotation)
	override fun setThickness(thickness: Float) = dataTracker.set(thicknessDataTracker, thickness)
	override fun setPigment(pigment: FrozenColorizer) = dataTracker.set(pigmentDataTracker, pigment.serializeToNBT())
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)
}