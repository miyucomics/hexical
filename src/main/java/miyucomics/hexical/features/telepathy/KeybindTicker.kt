package miyucomics.hexical.features.telepathy

import miyucomics.hexical.features.player.types.PlayerTicker
import net.minecraft.entity.player.PlayerEntity

class KeybindTicker : PlayerTicker {
	override fun tick(player: PlayerEntity) {
		player.serverKeybindActive().keys.filter { player.serverKeybindActive()[it] == true }.forEach { key ->
			player.serverKeybindDuration()[key] = player.serverKeybindDuration()[key]!! + 1
		}
	}
}