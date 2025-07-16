package miyucomics.hexical.features.media_log

import miyucomics.hexical.features.media_log.MediaLogField.Companion.MEDIA_LOG_CHANNEL
import miyucomics.hexical.inits.InitHook
import miyucomics.hexical.misc.ClientStorage
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object ClientMediaLogReceiver : InitHook() {
	override fun init() {
		ClientPlayNetworking.registerGlobalReceiver(MEDIA_LOG_CHANNEL) { _, _, packet, _ ->
			ClientStorage.mediaLog = MediaLogField().also { it.fromNbt(packet.readNbt()!!) }
		}
	}
}