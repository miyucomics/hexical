package miyucomics.hexical.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import miyucomics.hexical.Hexical;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import static miyucomics.hexical.Hexical.id;

public class HexicalItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Hexical.MOD_ID, Registry.ITEM_KEY);

	public static void init() {
		ITEMS.register();
	}

	public static final ItemGroup DUMMY_GROUP = CreativeTabRegistry.create(id("dummy_group"), () -> new ItemStack(HexicalItemRegistry.DUMMY_ITEM.get()));
	public static final RegistrySupplier<Item> DUMMY_ITEM = ITEMS.register("dummy_item", () -> new Item(new Item.Settings().group(DUMMY_GROUP)));
}