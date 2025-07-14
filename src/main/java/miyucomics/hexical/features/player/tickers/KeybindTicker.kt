package miyucomics.hexical.features.player.tickers

import miyucomics.hexical.features.player.fields.serverKeybindActive
import miyucomics.hexical.features.player.fields.serverKeybindDuration
import miyucomics.hexical.features.player.types.PlayerTicker
import net.minecraft.entity.player.PlayerEntity

class KeybindTicker : PlayerTicker {
	override fun tick(player: PlayerEntity) {
		player.serverKeybindActive().keys.filter { player.serverKeybindActive()[it] == true }.forEach { key ->
			player.serverKeybindDuration()[key] = player.serverKeybindDuration()[key]!! + 1
		}
	}
}