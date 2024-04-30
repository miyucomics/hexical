package miyucomics.hexical.registry

import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.items.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object HexicalItems {
	private val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Hexical.MOD_ID, Registry.ITEM_KEY)
	val HEXICAL_GROUP: ItemGroup = CreativeTabRegistry.create(Hexical.id("general")) { LAMP_ITEM.defaultStack }
	val LAMP_ITEM: LampItem = LampItem()
	val ARCH_LAMP_ITEM: ArchLampItem = ArchLampItem()
	val GRIMOIRE_ITEM: GrimoireItem = GrimoireItem()
	val CONJURED_STAFF_ITEM: ConjuredStaffItem = ConjuredStaffItem()
	val LIGHTNING_ROD_STAFF: LightningRodStaff = LightningRodStaff()

	@JvmStatic
	fun init() {
		ITEMS.register("grimoire") { GRIMOIRE_ITEM }
		ITEMS.register("lamp") { LAMP_ITEM }
		ITEMS.register("arch_lamp") { ARCH_LAMP_ITEM }
		ITEMS.register("living_scroll_small") { LivingScrollItem(1) }
		ITEMS.register("living_scroll_large") { LivingScrollItem(3) }
		ITEMS.register("lightning_rod_staff") { LIGHTNING_ROD_STAFF }
		ITEMS.register("conjured_staff") { CONJURED_STAFF_ITEM }
		ITEMS.register("mage_block") { BlockItem(HexicalBlocks.MAGE_BLOCK, Settings()) }
		ITEMS.register()
	}
}