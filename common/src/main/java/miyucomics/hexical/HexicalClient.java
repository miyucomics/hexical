package miyucomics.hexical;

import miyucomics.hexical.registry.HexicalEntities;
import miyucomics.hexical.registry.HexicalKeybinds;

public class HexicalClient {
	public static void init() {
		HexicalEntities.clientInit();
		HexicalKeybinds.init();
	}
}