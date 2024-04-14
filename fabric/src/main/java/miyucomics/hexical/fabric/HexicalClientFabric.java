package miyucomics.hexical.fabric;

import miyucomics.hexical.networking.SpawnLivingScrollPacket;
import net.fabricmc.api.ClientModInitializer;
import miyucomics.hexical.HexicalClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Consumer;
import java.util.function.Function;

public class HexicalClientFabric implements ClientModInitializer {
	@Override public void onInitializeClient() {
		HexicalClient.init();
		ClientPlayNetworking.registerGlobalReceiver(SpawnLivingScrollPacket.Companion.getID(), makeClientBoundHandler(SpawnLivingScrollPacket.Companion::deserialize, SpawnLivingScrollPacket.Companion::handle));
	}

	private static <T> ClientPlayNetworking.PlayChannelHandler makeClientBoundHandler(Function<PacketByteBuf, T> decoder, Consumer<T> handler) {
		return (_client, _handler, buf, _responseSender) -> handler.accept(decoder.apply(buf));
	}
}