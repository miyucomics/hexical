package miyucomics.hexical.data

import java.util.*

object KeybindData {
	val active: HashMap<UUID, HashMap<String, Boolean>> = HashMap()
	val duration: HashMap<UUID, HashMap<String, Int>> = HashMap()
	val scroll: HashMap<UUID, Int> = HashMap()
}