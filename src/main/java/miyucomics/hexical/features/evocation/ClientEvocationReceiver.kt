package miyucomics.hexical.features.evocation

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object ClientEvocationReceiver : InitHook() {
	override fun init() {
		ClientPlayNetworking.registerGlobalReceiver(ServerEvocationManager.START_EVOKE_CHANNEL) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
			val container = (player as PlayerAnimations).hexicalModAnimations()
			container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_loop"))!!))
			player.evocationActive = true
		}

		ClientPlayNetworking.registerGlobalReceiver(ServerEvocationManager.END_EVOKING_CHANNEL) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
			val container = (player as PlayerAnimations).hexicalModAnimations()
			container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_end"))!!))
			player.evocationActive = false
		}
	}
}