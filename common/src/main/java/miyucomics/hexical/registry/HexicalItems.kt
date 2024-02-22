package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.items.LampItem
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object HexicalItems {
	private val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Hexical.MOD_ID, Registry.ITEM_KEY)
	val LAMP_ITEM: LampItem = LampItem()

	@JvmStatic
	fun init() {
		ITEMS.register("lamp") { LAMP_ITEM }
		ITEMS.register("conjured_bouncy_block") { BlockItem(HexicalBlocks.CONJURED_BOUNCY_BLOCK, Item.Settings()) }
		ITEMS.register("conjured_slippery_block") { BlockItem(HexicalBlocks.CONJURED_SLIPPERY_BLOCK, Item.Settings()) }
		ITEMS.register("conjured_volatile_block") { BlockItem(HexicalBlocks.CONJURED_VOLATILE_BLOCK, Item.Settings()) }
		ITEMS.register()
	}
}