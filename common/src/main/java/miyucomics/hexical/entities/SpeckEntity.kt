package miyucomics.hexical.entities

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World

val patternDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
val pigmentDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)

class SpeckEntity(entityType: EntityType<SpeckEntity?>?, world: World?) : Entity(entityType, world) {
	private var pattern: HexPattern = HexPattern.fromAngles("w", HexDir.WEST)
	private var pigment: FrozenColorizer = FrozenColorizer.DEFAULT.get()

	override fun initDataTracker() {
		dataTracker.startTracking(patternDataTracker, NbtCompound())
		dataTracker.startTracking(pigmentDataTracker, NbtCompound())
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		pattern = HexPattern.fromNBT(nbt.getCompound("pattern"))
		pigment = FrozenColorizer.fromNBT(nbt.getCompound("pigment"))
		dataTracker.set(patternDataTracker, nbt.getCompound("pattern"))
		dataTracker.set(pigmentDataTracker, nbt.getCompound("pigment"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("pattern", pattern.serializeToNBT())
		nbt.putCompound("pigment", pigment.serializeToNBT())
	}

	fun setPattern(pattern: HexPattern) {
		this.pattern = pattern
		dataTracker.set(patternDataTracker, pattern.serializeToNBT())
	}

	fun setPigment(pigment: FrozenColorizer) {
		this.pigment = pigment
		dataTracker.set(pigmentDataTracker, pigment.serializeToNBT())
	}

	fun getPattern(): HexPattern {
		return HexPattern.fromNBT(dataTracker.get(patternDataTracker))
	}

	fun getPigment(): FrozenColorizer {
		return FrozenColorizer.fromNBT(dataTracker.get(pigmentDataTracker))
	}

	override fun createSpawnPacket(): Packet<*> {
		return EntitySpawnS2CPacket(this)
	}
}