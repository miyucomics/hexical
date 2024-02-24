package miyucomics.hexical.persistent_state

import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.api.utils.vecFromNBT
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d

class MasterLampData {
	var position: Vec3d = Vec3d.ZERO
	var rotation: Vec3d = Vec3d.ZERO
	var velocity: Vec3d = Vec3d.ZERO
	var time: Long = 0
	var active: Boolean = false

	fun toNbt(): NbtCompound {
		val tag = NbtCompound()
		tag.put("position", position.serializeToNBT())
		tag.put("rotation", rotation.serializeToNBT())
		tag.put("velocity", velocity.serializeToNBT())
		tag.putLong("time", time)
		tag.putBoolean("active", active)
		return tag
	}

	companion object {
		fun createFromNbt(tag: NbtCompound): MasterLampData {
			val state = MasterLampData()
			state.position = vecFromNBT(tag.getLongArray("position"))
			state.rotation = vecFromNBT(tag.getLongArray("rotation"))
			state.velocity = vecFromNBT(tag.getLongArray("velocity"))
			state.time = tag.getLong("time")
			state.active = tag.getBoolean("active")
			return state
		}
	}
}