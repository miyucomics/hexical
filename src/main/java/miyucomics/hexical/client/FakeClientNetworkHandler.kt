package miyucomics.hexical.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.NetworkSide
import net.minecraft.network.Packet

class FakeClientPlayNetworkHandler(client: MinecraftClient) : ClientPlayNetworkHandler(client, null, FakeClientConnection(NetworkSide.CLIENTBOUND), client.session.profile, null) {
	override fun sendPacket(packet: Packet<*>?) {

	}
}