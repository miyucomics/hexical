package miyucomics.hexical;

import miyucomics.hexical.registry.*;
import net.minecraft.util.Identifier;

import java.util.Random;

public class Hexical {
	public static final String MOD_ID = "hexical";
	public static final Identifier CAST_CONJURED_STAFF_PACKET = new Identifier(MOD_ID, "cast_conjured_staff");
	public static final Identifier START_TELEPATHY_PACKET = new Identifier(MOD_ID, "start_telepathy");
	public static final Identifier STOP_TELEPATHY_PACKET = new Identifier(MOD_ID, "stop_telepathy");
	public static final Random RANDOM = new Random();

	public static void init() {
		HexicalAbstractions.initPlatformSpecific();
		HexicalAdvancements.init();
		HexicalBlocks.init();
		HexicalEntities.init();
		HexicalEvents.init();
		HexicalItems.init();
		HexicalPatterns.init();
		HexicalKeybinds.init();
		HexicalNetworking.init();
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}