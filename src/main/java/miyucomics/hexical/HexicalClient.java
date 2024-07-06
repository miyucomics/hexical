package miyucomics.hexical;

import miyucomics.hexical.registry.*;
import net.fabricmc.api.ClientModInitializer;

public class HexicalClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HexicalBlocks.clientInit();
		HexicalEntities.clientInit();
		HexicalEvents.clientInit();
		HexicalItems.clientInit();
		HexicalKeybinds.init();
		HexicalNetworking.clientInit();
	}
}