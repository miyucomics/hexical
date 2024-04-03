package miyucomics.hexical.interfaces

import net.minecraft.entity.player.PlayerEntity

interface Possessable {
	fun getPossessor(): PlayerEntity?
	fun setPossessor(possessor: PlayerEntity)
}