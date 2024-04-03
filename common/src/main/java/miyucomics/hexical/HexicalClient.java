package miyucomics.hexical;

import miyucomics.hexical.registry.HexicalBlocks;
import miyucomics.hexical.registry.HexicalEntities;
import miyucomics.hexical.registry.HexicalKeybinds;

public class HexicalClient {
	public static void init() {
		HexicalBlocks.clientInit();
		HexicalEntities.clientInit();
		HexicalKeybinds.init();
	}
}