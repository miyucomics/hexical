package miyucomics.hexical.registry

import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.KeybindData
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

object HexicalEvents {
	@JvmStatic
	fun init() {
		ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
			val player = handler.player.uuid
			EvokeState.active[player] = false
			EvokeState.duration[player] = -1
			if (KeybindData.active.containsKey(player)) {
				for (key in KeybindData.active[player]!!.keys) {
					KeybindData.active[player]!![key] = false
					KeybindData.duration[player]!![key] = 0
				}
			}
		}
		ServerTickEvents.END_SERVER_TICK.register {
			for (player in EvokeState.active.keys)
				if (EvokeState.active[player]!!)
					EvokeState.duration[player] = EvokeState.duration[player]!! + 1
			for (player in KeybindData.duration.keys) {
				val binds = KeybindData.active[player]!!
				for (key in binds.keys)
					if (KeybindData.active[player]!!.getOrDefault(key, false))
						KeybindData.duration[player]!![key] = KeybindData.duration[player]!![key]!! + 1
			}
		}
	}
}