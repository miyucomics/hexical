package miyucomics.hexical.features.player_state

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound

interface PlayerField {
	fun tick(player: PlayerEntity) {}
	fun readNbt(compound: NbtCompound) {}
	fun writeNbt(compound: NbtCompound) {}
	fun handleRespawn(new: PlayerEntity, old: PlayerEntity) {}
}