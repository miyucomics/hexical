package miyucomics.hexical;

import miyucomics.hexical.registry.HexicalIotaTypeRegistry;
import miyucomics.hexical.registry.HexicalItemRegistry;
import miyucomics.hexical.registry.HexicalPatternRegistry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class Hexical {
    public static final String MOD_ID = "hexical";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public static void init() {
        LOGGER.info("Hexical says hello!");

        HexicalAbstractions.initPlatformSpecific();
        HexicalItemRegistry.init();
        HexicalIotaTypeRegistry.init();
        HexicalPatternRegistry.init();

        LOGGER.info(HexicalAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
}
