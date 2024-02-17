package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.items.LampItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object HexicalItems {
	private val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Hexical.MOD_ID, Registry.ITEM_KEY)
	val LAMP_ITEM: LampItem = LampItem()

	@JvmStatic
	fun init() {
		ITEMS.register("lamp") { LAMP_ITEM }
		ITEMS.register()
	}
}