package miyucomics.hexical.features.player

import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents

object RespawnPersistHook : InitHook() {
	override fun init() {
		ServerPlayerEvents.AFTER_RESPAWN.register { old, new, alive ->
			new.getHexicalPlayerManager().handleRespawn(new, old)
		}
	}
}