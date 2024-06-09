package miyucomics.hexical.state

import java.util.UUID

object EvokeState {
	val active: HashMap<UUID, Boolean> = HashMap()
	val duration: HashMap<UUID, Int> = HashMap()
}