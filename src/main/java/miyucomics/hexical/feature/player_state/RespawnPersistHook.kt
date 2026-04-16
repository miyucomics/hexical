package miyucomics.hexical.feature.player_state

import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents

object RespawnPersistHook : InitHook() {
	override fun init() {
		ServerPlayerEvents.AFTER_RESPAWN.register { old, new, _ ->
			new.getHexicalPlayerState().handleRespawn(new, old)
		}
	}
}