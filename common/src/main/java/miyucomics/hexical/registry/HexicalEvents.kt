package miyucomics.hexical.registry

import dev.architectury.event.events.common.TickEvent
import miyucomics.hexical.state.KeybindData
import miyucomics.hexical.state.TelepathyData

object HexicalEvents {
	@JvmStatic
	fun init() {
		TickEvent.SERVER_POST.register(TickEvent.Server {
			for (key in TelepathyData.timer.keys)
				if (TelepathyData.active[key]!!)
					TelepathyData.timer[key] = TelepathyData.timer[key]!! + 1
			for (player in KeybindData.duration.keys) {
				val binds = KeybindData.active[player]!!
				for (key in binds.keys)
					if (KeybindData.active[player]!!.getOrDefault(key, false))
						KeybindData.duration[player]!![key] = KeybindData.duration[player]!![key]!! + 1
			}
		})
	}
}