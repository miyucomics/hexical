package miyucomics.hexical.features.lesser_sentinel

import miyucomics.hexical.inits.Hook
import miyucomics.hexical.misc.ClientStorage
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.util.math.Vec3d

object ClientLesserSentinelReceiver : Hook() {
	override fun registerCallbacks() {
		ClientPlayNetworking.registerGlobalReceiver(ServerLesserSentinelPusher.LESSER_SENTINEL_CHANNEL) { client, _, packet, _ ->
			val count = packet.readInt()
			val list = mutableListOf<Vec3d>()

			repeat(count) {
				val x = packet.readDouble()
				val y = packet.readDouble()
				val z = packet.readDouble()
				list.add(Vec3d(x, y, z))
			}

			client.execute {
				ClientStorage.lesserSentinels.clear()
				ClientStorage.lesserSentinels.addAll(list)
			}
		}
	}
}