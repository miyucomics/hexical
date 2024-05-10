package miyucomics.hexical;

import miyucomics.hexical.registry.*;
import net.minecraft.util.Identifier;

import java.util.Random;

public class Hexical {
	public static final String MOD_ID = "hexical";
	public static final Identifier CAST_CONJURED_STAFF_PACKET = id("cast_conjured_staff");
	public static final Identifier START_TELEPATHY_PACKET = id("start_telepathy");
	public static final Identifier STOP_TELEPATHY_PACKET = id("stop_telepathy");
	public static final Identifier PRESSED_KEY = id("press_key");
	public static final Identifier RELEASED_KEY = id("release_key");
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