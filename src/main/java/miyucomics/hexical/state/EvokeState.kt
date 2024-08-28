package miyucomics.hexical.state

import java.util.*

object EvokeState {
	val active: HashMap<UUID, Boolean> = HashMap()
	val duration: HashMap<UUID, Int> = HashMap()

	@JvmStatic
	fun isEvoking(uuid: UUID): Boolean = active.getOrDefault(uuid, false)
}