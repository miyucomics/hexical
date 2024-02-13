package miyucomics.hexical.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class HexicalAbstractionsImpl {
	public static Path getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get();
	}

	public static void initPlatformSpecific() {
		HexicalConfigForge.init();
	}
}