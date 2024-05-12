package miyucomics.hexical;

import miyucomics.hexical.registry.*;
import net.minecraft.util.Identifier;

import java.util.Random;

public class Hexical {
	public static final String MOD_ID = "hexical";
	public static final Random RANDOM = new Random();

	public static void init() {
		HexicalAbstractions.initPlatformSpecific();
		HexicalBlocks.init();
		HexicalEntities.init();
		HexicalEvents.init();
		HexicalIota.init();
		HexicalItems.init();
		HexicalPatterns.init();
		HexicalNetworking.init();
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}