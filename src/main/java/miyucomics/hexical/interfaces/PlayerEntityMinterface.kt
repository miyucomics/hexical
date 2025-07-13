package miyucomics.hexical.interfaces

import miyucomics.hexical.features.player.PlayerManager

interface PlayerEntityMinterface {
	fun getPlayerManager(): PlayerManager
}