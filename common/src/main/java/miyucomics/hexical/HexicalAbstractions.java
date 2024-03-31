package miyucomics.hexical;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class HexicalAbstractions {
	@ExpectPlatform
	public static void initPlatformSpecific() {
		throw new AssertionError();
	}
}