package miyucomics.hexical;

import miyucomics.hexical.registry.HexicalBlockRegistry;
import miyucomics.hexical.registry.HexicalIotaTypeRegistry;
import miyucomics.hexical.registry.HexicalItemRegistry;
import miyucomics.hexical.registry.HexicalPatternRegistry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hexical {
	public static final String MOD_ID = "hexical";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static void init() {
		HexicalAbstractions.initPlatformSpecific();
		HexicalBlockRegistry.init();
		HexicalItemRegistry.init();
		HexicalIotaTypeRegistry.init();
		HexicalPatternRegistry.init();
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}