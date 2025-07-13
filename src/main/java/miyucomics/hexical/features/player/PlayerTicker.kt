package miyucomics.hexical.features.player

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound

interface PlayerTicker {
	fun tick(player: PlayerEntity) {}
}