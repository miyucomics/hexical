package miyucomics.hexical.registry;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import kotlin.Triple;
import miyucomics.hexical.casting.patterns.spells.OpChorusBlink;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static miyucomics.hexical.Hexical.id;

public class HexicalPatternRegistry {
	public static List<Triple<HexPattern, Identifier, Action>> PATTERNS = new ArrayList<>();
	public static List<Triple<HexPattern, Identifier, Action>> PER_WORLD_PATTERNS = new ArrayList<>();
	public static HexPattern CHORUS_BLINK = register(HexPattern.fromAngles("aawqqqq", HexDir.SOUTH_EAST), "chorus_blink", new OpChorusBlink());

	public static void init() {
		try {
			for (Triple<HexPattern, Identifier, Action> patternTriple : PATTERNS) {
				PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird());
			}
			for (Triple<HexPattern, Identifier, Action> patternTriple : PER_WORLD_PATTERNS) {
				PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird(), true);
			}
		} catch (PatternRegistry.RegisterPatternException e) {
			e.printStackTrace();
		}
	}

	private static HexPattern register(HexPattern pattern, String name, Action action) {
		Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
		PATTERNS.add(triple);
		return pattern;
	}

	private static HexPattern registerPerWorld(HexPattern pattern, String name, Action action) {
		Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
		PER_WORLD_PATTERNS.add(triple);
		return pattern;
	}
}