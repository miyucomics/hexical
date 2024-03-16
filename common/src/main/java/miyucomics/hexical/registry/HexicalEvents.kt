package miyucomics.hexical.registry

import dev.architectury.event.events.common.TickEvent
import miyucomics.hexical.persistent_state.TelepathyData

object HexicalEvents {
	@JvmStatic
	fun init() {
		TickEvent.SERVER_POST.register(TickEvent.Server {
			for (key in TelepathyData.timer.keys)
				if (TelepathyData.active[key]!!)
					TelepathyData.timer[key] = TelepathyData.timer[key]!! + 1
		})
	}
}