package miyucomics.hexical.forge;

import miyucomics.hexical.HexicalClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Forge client loading entrypoint.
 */
public class HexicalClientForge {
    public static void init(FMLClientSetupEvent event) {
        HexicalClient.init();
    }
}
