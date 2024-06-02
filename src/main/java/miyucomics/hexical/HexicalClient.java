package miyucomics.hexical;

import miyucomics.hexical.registry.HexicalEntities;
import miyucomics.hexical.registry.HexicalKeybinds;
import miyucomics.hexical.registry.HexicalNetworking;
import net.fabricmc.api.ClientModInitializer;

public class HexicalClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HexicalEntities.clientInit();
		HexicalKeybinds.init();
		HexicalNetworking.clientInit();
	}
}