package miyucomics.hexical.fabric;

import net.fabricmc.api.ClientModInitializer;
import miyucomics.hexical.HexicalClient;

public class HexicalClientFabric implements ClientModInitializer {
	@Override public void onInitializeClient() {
		HexicalClient.init();
	}
}