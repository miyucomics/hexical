package miyucomics.hexical.features.player.fields

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.api.utils.vecFromNBT
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.features.player.types.PlayerField
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d

class ArchLampField : PlayerField {
	var position: Vec3d = Vec3d.ZERO
	var rotation: Vec3d = Vec3d.ZERO
	var velocity: Vec3d = Vec3d.ZERO
	var storage: NbtCompound = NbtCompound()
	var time: Long = 0

	override fun readNbt(compound: NbtCompound) {
		if (!compound.contains("arch_lamp"))
			return
		val archLampData = compound.getCompound("arch_lamp")
		this.position = vecFromNBT(archLampData.getLongArray("position"))
		this.rotation = vecFromNBT(archLampData.getLongArray("rotation"))
		this.velocity = vecFromNBT(archLampData.getLongArray("velocity"))
		this.storage = archLampData.getCompound("storage")
		this.time = archLampData.getLong("time")
	}

	override fun writeNbt(compound: NbtCompound) {
		compound.putCompound("arch_lamp", NbtCompound().also {
			it.put("position", this.position.serializeToNBT())
			it.put("rotation", this.rotation.serializeToNBT())
			it.put("velocity", this.velocity.serializeToNBT())
			it.putCompound("storage", this.storage)
			it.putLong("time", this.time)
		})
	}
}

fun PlayerEntity.getArchLampField() = this.getHexicalPlayerManager().get(ArchLampField::class)