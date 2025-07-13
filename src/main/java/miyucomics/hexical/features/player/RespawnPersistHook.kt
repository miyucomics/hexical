package miyucomics.hexical.features.player

import miyucomics.hexical.inits.Hook
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents

object RespawnPersistHook : Hook() {
	override fun registerCallbacks() {
		ServerPlayerEvents.AFTER_RESPAWN.register { old, new, alive ->
			new.getHexicalPlayerManager().handleRespawn(new, old)
		}
	}
}