package miyucomics.hexical.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import miyucomics.hexical.Hexical;
import miyucomics.hexical.items.LampItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class HexicalItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Hexical.MOD_ID, Registry.ITEM_KEY);
	public static final LampItem LAMP_ITEM = new LampItem();

	public static void init() {
		ITEMS.register("lamp", () -> LAMP_ITEM);
		ITEMS.register();
	}
}