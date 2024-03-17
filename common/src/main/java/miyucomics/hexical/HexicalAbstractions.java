package miyucomics.hexical;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class HexicalAbstractions {
	@ExpectPlatform
	public static void initPlatformSpecific() {
		throw new AssertionError();
	}
}