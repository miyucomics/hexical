package miyucomics.hexical.state

import java.util.*

object KeybindData {
	val active: HashMap<UUID, HashMap<String, Boolean>> = HashMap()
	val duration: HashMap<UUID, HashMap<String, Int>> = HashMap()
}