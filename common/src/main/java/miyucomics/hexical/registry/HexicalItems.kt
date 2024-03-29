package miyucomics.hexical.registry

import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.items.ConjuredStaffItem
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.items.LampItem
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object HexicalItems {
	private val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Hexical.MOD_ID, Registry.ITEM_KEY)
	val HEXICAL_GROUP: ItemGroup = CreativeTabRegistry.create(Hexical.id("general")) { LAMP_ITEM.getDefaultStack() }
	val LAMP_ITEM: LampItem = LampItem()
	val ARCH_LAMP_ITEM: ArchLampItem = ArchLampItem()
	val GRIMOIRE_ITEM: GrimoireItem = GrimoireItem()
	val CONJURED_STAFF_ITEM: ConjuredStaffItem = ConjuredStaffItem()

	@JvmStatic
	fun init() {
		ITEMS.register("lamp") { LAMP_ITEM }
		ITEMS.register("grimoire") { GRIMOIRE_ITEM }
		ITEMS.register("arch_lamp") { ARCH_LAMP_ITEM }
		ITEMS.register("conjured_staff") { CONJURED_STAFF_ITEM }
		ITEMS.register("mage_block") { BlockItem(HexicalBlocks.MAGE_BLOCK, Item.Settings()) }
		ITEMS.register()
	}
}