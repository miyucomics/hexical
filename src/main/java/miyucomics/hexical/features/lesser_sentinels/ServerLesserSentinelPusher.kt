package miyucomics.hexical.features.lesser_sentinels

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.player.fields.syncLesserSentinels
import miyucomics.hexical.inits.Hook
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

object ServerLesserSentinelPusher : Hook() {
	val LESSER_SENTINEL_CHANNEL = HexicalMain.id("lesser_sentinels")

	override fun registerCallbacks() {
		ServerPlayConnectionEvents.JOIN.register { handler, _, _ -> handler.player.syncLesserSentinels() }
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register { player, _, _ -> player.syncLesserSentinels() }
	}
}