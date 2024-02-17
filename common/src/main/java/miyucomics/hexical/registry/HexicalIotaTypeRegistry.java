package miyucomics.hexical.registry;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import static miyucomics.hexical.Hexical.id;

public class HexicalIotaTypeRegistry {
	public static Map<Identifier, IotaType<?>> TYPES = new HashMap<>();

	public static void init() {
		for (Map.Entry<Identifier, IotaType<?>> entry : TYPES.entrySet()) {
			Registry.register(HexIotaTypes.REGISTRY, entry.getKey(), entry.getValue());
		}
	}

	private static <U extends Iota, T extends IotaType<U>> T register(String name, T type) {
		IotaType<?> old = TYPES.put(id(name), type);
		if (old != null)
			throw new IllegalArgumentException("Typo? Duplicate id " + name);
		return type;
	}
}