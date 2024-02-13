package miyucomics.hexical.fabric;

import net.fabricmc.api.ModInitializer;
import miyucomics.hexical.Hexical;

public class HexicalFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Hexical.init();
	}
}