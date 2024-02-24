package miyucomics.hexical.persistent_state

import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.api.utils.vecFromNBT
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d

class MasterLampData {
	var startPosition: Vec3d = Vec3d.ZERO
	var startRotation: Vec3d = Vec3d.ZERO
	var startTime: Long = 0
	var active: Boolean = false

	fun toNbt(): NbtCompound {
		val tag = NbtCompound()
		tag.put("startPosition", startPosition.serializeToNBT())
		tag.put("startRotation", startRotation.serializeToNBT())
		tag.putLong("startTime", startTime)
		tag.putBoolean("active", active)
		return tag
	}

	companion object {
		fun createFromNbt(tag: NbtCompound): MasterLampData {
			val state = MasterLampData()
			state.startPosition = vecFromNBT(tag.getLongArray("startPosition"))
			state.startRotation = vecFromNBT(tag.getLongArray("startRotation"))
			state.startTime = tag.getLong("startTime")
			state.active = tag.getBoolean("active")
			return state
		}
	}
}