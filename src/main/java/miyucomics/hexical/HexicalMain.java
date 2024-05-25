package miyucomics.hexical;

import miyucomics.hexical.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.util.Random;

public class HexicalMain implements ModInitializer {
	public static final String MOD_ID = "hexical";
	public static final Random RANDOM = new Random();

	@Override
	public void onInitialize() {
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