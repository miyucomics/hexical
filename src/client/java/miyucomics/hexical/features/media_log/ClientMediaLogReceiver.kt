package miyucomics.hexical.features.media_log

import miyucomics.hexical.ClientStorage
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object ClientMediaLogReceiver : InitHook() {
	override fun init() {
		ClientPlayNetworking.registerGlobalReceiver(MediaLogField.MEDIA_LOG_CHANNEL) { _, _, packet, _ ->
			ClientStorage.mediaLog = MediaLogField().also { it.fromNbt(packet.readNbt()!!) }
		}
	}
}