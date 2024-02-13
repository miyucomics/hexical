package miyucomics.hexical.fabric;

import net.fabricmc.loader.api.FabricLoader;
import miyucomics.hexical.HexicalAbstractions;

import java.nio.file.Path;

public class HexicalAbstractionsImpl {
    /**
     * This is the actual implementation of {@link HexicalAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
	
    public static void initPlatformSpecific() {
        HexicalConfigFabric.init();
    }
}
