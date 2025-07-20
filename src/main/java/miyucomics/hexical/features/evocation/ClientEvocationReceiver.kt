package miyucomics.hexical.features.evocation

import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object ClientEvocationReceiver : InitHook() {
	override fun init() {
		ClientPlayNetworking.registerGlobalReceiver(ServerEvocationManager.START_EVOKE_CHANNEL) { client, _, packet, _ ->
			val player = client.world!!.getPlayerByUuid(packet.readUuid()) ?: return@registerGlobalReceiver
			player.evocationActive = true
		}

		ClientPlayNetworking.registerGlobalReceiver(ServerEvocationManager.END_EVOKING_CHANNEL) { client, _, packet, _ ->
			val player = client.world!!.getPlayerByUuid(packet.readUuid()) ?: return@registerGlobalReceiver
			player.evocationActive = false
		}
	}
}