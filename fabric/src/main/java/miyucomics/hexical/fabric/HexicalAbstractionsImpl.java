package miyucomics.hexical.fabric;

import net.fabricmc.loader.api.FabricLoader;
import miyucomics.hexical.HexicalAbstractions;

import java.nio.file.Path;

public class HexicalAbstractionsImpl {
	public static Path getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir();
	}

	public static void initPlatformSpecific() {
		HexicalConfigFabric.init();
	}
}