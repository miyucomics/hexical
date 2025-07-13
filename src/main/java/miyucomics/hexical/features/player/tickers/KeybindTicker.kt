package miyucomics.hexical.features.player.tickers

import miyucomics.hexical.features.player.PlayerTicker
import net.minecraft.entity.player.PlayerEntity

class KeybindTicker : PlayerTicker {
	override fun tick(player: PlayerEntity) {
		this.active.keys.filter { this.active[it] == true }.forEach { key ->
			this.duration[key] = this.duration[key]!! + 1
		}
	}
}