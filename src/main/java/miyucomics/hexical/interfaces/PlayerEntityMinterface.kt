package miyucomics.hexical.interfaces

import miyucomics.hexical.features.player_state.PlayerState

interface PlayerEntityMinterface {
	fun getPlayerState(): PlayerState
}