package miyucomics.hexical.features.player.types

import net.minecraft.entity.player.PlayerEntity

interface PlayerTicker {
	fun tick(player: PlayerEntity) {}
}