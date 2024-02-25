package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.items.ConjuredStaffItem
import miyucomics.hexical.items.LampItem
import miyucomics.hexical.items.MasterLampItem
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object HexicalItems {
	private val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Hexical.MOD_ID, Registry.ITEM_KEY)
	val LAMP_ITEM: LampItem = LampItem()
	val MASTER_LAMP_ITEM: MasterLampItem = MasterLampItem()
	val TARNISHED_LAMP_ITEM: Item = Item(Item.Settings().maxCount(1))
	val CONJURED_STAFF_ITEM: ConjuredStaffItem = ConjuredStaffItem()

	@JvmStatic
	fun init() {
		ITEMS.register("lamp") { LAMP_ITEM }
		ITEMS.register("master_lamp") { MASTER_LAMP_ITEM }
		ITEMS.register("tarnished_lamp") { TARNISHED_LAMP_ITEM }
		ITEMS.register("conjured_staff") { CONJURED_STAFF_ITEM }
		ITEMS.register("advanced_conjured_block") { BlockItem(HexicalBlocks.ADVANCED_CONJURED_BLOCK, Item.Settings()) }
		ITEMS.register()
	}
}