package miyucomics.hexical.entities

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class SpikeEntity(entityType: EntityType<SpikeEntity?>?, world: World?) : Entity(entityType, world) {
	private var direction: Direction = Direction.UP
	companion object {
	}

	fun setDirection() {
		if (this.direction.axis.isHorizontal) {
			this.pitch = 0.0f
			this.yaw = (this.direction.horizontal * 90).toFloat()
		} else {
			this.pitch = (-90 * this.direction.direction.offset()).toFloat()
			this.yaw = 0.0f
		}
	}

	override fun initDataTracker() {
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
	}

	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)
}