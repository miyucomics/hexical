package miyucomics.hexical.data

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.api.utils.vecFromNBT
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d

class ArchLampState {
	var position: Vec3d = Vec3d.ZERO
	var rotation: Vec3d = Vec3d.ZERO
	var velocity: Vec3d = Vec3d.ZERO
	var storage: NbtCompound = NbtCompound()
	var time: Long = 0

	fun toNbt(): NbtCompound {
		val tag = NbtCompound()
		tag.put("position", position.serializeToNBT())
		tag.put("rotation", rotation.serializeToNBT())
		tag.put("velocity", velocity.serializeToNBT())
		tag.putCompound("storage", storage)
		tag.putLong("time", time)
		return tag
	}

	companion object {
		@JvmStatic
		fun createFromNbt(tag: NbtCompound): ArchLampState {
			val state = ArchLampState()
			state.position = vecFromNBT(tag.getLongArray("position"))
			state.rotation = vecFromNBT(tag.getLongArray("rotation"))
			state.velocity = vecFromNBT(tag.getLongArray("velocity"))
			state.storage = tag.getCompound("storage")
			state.time = tag.getLong("time")
			return state
		}
	}
}