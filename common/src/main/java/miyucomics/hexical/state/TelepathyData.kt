package miyucomics.hexical.state

import java.util.*

object TelepathyData {
	val active: HashMap<UUID, Boolean> = HashMap<UUID, Boolean>()
	val timer: HashMap<UUID, Int> = HashMap<UUID, Int>()
}