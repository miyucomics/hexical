package miyucomics.hexical.features.player.types

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound

interface PlayerField {
	fun readNbt(compound: NbtCompound) {}
	fun writeNbt(compound: NbtCompound) {}
	fun handleRespawn(new: PlayerEntity, old: PlayerEntity) {}
}