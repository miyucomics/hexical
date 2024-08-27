package miyucomics.hexical;

import miyucomics.hexical.data.PrestidigitationData;
import miyucomics.hexical.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.util.Random;

public class HexicalMain implements ModInitializer {
	public static final String MOD_ID = "hexical";
	public static final Random RANDOM = new Random();
	public static final Integer EVOKE_DURATION = 20;

	@Override
	public void onInitialize() {
		HexicalAdvancements.init();
		HexicalBlocks.init();
		HexicalData.init();
		HexicalEntities.init();
		HexicalEvents.init();
		HexicalIota.init();
		HexicalItems.init();
		HexicalSounds.init();
		HexicalPatterns.init();
		HexicalNetworking.serverInit();
		PrestidigitationData.init();
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}