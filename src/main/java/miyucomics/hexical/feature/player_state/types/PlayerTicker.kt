package miyucomics.hexical.feature.player_state.types

import net.minecraft.entity.player.PlayerEntity

interface PlayerTicker {
	fun tick(player: PlayerEntity) {}
}