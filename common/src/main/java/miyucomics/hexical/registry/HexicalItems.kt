package miyucomics.hexical.registry

import at.petrak.hexcasting.common.items.ItemStaff
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
	val LIVING_SCROLL_SMALL_ITEM: LivingScrollItem = LivingScrollItem(1)
	val LIVING_SCROLL_MEDIUM_ITEM: LivingScrollItem = LivingScrollItem(2)
	val LIVING_SCROLL_LARGE_ITEM: LivingScrollItem = LivingScrollItem(3)

	@JvmStatic
	fun init() {
		ITEMS.register("copper_staff") { ItemStaff(Settings().maxCount(1).group(HEXICAL_GROUP)) }

		ITEMS.register("grimoire") { GRIMOIRE_ITEM }
		ITEMS.register("lamp") { LAMP_ITEM }
		ITEMS.register("arch_lamp") { ARCH_LAMP_ITEM }
		ITEMS.register("living_scroll_small") { LIVING_SCROLL_SMALL_ITEM }
		ITEMS.register("living_scroll_medium") { LIVING_SCROLL_MEDIUM_ITEM }
		ITEMS.register("living_scroll_large") { LIVING_SCROLL_LARGE_ITEM }
		ITEMS.register("conjured_staff") { CONJURED_STAFF_ITEM }
		ITEMS.register("mage_block") { BlockItem(HexicalBlocks.MAGE_BLOCK, Item.Settings()) }
		ITEMS.register()
	}
}