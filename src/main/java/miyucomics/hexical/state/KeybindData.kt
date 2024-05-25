package miyucomics.hexical.state

import java.util.UUID

object KeybindData {
	val active: HashMap<UUID, HashMap<String, Boolean>> = HashMap()
	val duration: HashMap<UUID, HashMap<String, Int>> = HashMap()
}