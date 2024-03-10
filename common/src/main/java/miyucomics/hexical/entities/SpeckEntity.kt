package miyucomics.hexical.entities

import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.client.getCenteredPattern
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.Vec2f
import net.minecraft.world.World

val patternNbtDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(SpeckEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)

class SpeckEntity(entityType: EntityType<SpeckEntity?>?, world: World?) : Entity(entityType, world) {
	private var pattern: HexPattern = HexPattern.fromAngles("w", HexDir.WEST)

	override fun initDataTracker() {
		dataTracker.startTracking(patternNbtDataTracker, NbtCompound())
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		pattern = HexPattern.fromNBT(nbt.getCompound("pattern"))
		dataTracker.set(patternNbtDataTracker, nbt.getCompound("pattern"))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putCompound("pattern", pattern.serializeToNBT())
	}

	fun setPattern(pattern: HexPattern) {
		this.pattern = pattern
		dataTracker.set(patternNbtDataTracker, pattern.serializeToNBT())
	}

	fun getPattern(): List<Vec2f> {
		return getCenteredPattern(HexPattern.fromNBT(dataTracker.get(patternNbtDataTracker)), 1f, 1f, 0.25f).second
	}

	override fun createSpawnPacket(): Packet<*> {
		return EntitySpawnS2CPacket(this)
	}
}