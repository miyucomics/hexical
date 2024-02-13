package miyucomics.hexical.forge;

import miyucomics.hexical.HexicalClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class HexicalClientForge {
	public static void init(FMLClientSetupEvent event) {
		HexicalClient.init();
	}
}