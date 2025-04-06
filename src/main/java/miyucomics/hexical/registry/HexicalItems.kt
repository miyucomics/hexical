package miyucomics.hexical.registry

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.items.ItemStaff
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.items.*
import miyucomics.hexical.misc.LeiArmorMaterial
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.client.item.CompassAnglePredicateProvider
import net.minecraft.client.item.CompassAnglePredicateProvider.CompassTarget
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.item.*
import net.minecraft.item.Item.Settings
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos

object HexicalItems {
	private val HEXICAL_GROUP_KEY: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, HexicalMain.id("general"))
	val HEXICAL_GROUP: ItemGroup = FabricItemGroup.builder().icon { ItemStack(TCHOTCHKE_ITEM) }.displayName(Text.translatable("itemGroup.hexical.general")).build()

	@JvmField
	val HAND_LAMP_ITEM = HandLampItem()
	@JvmField
	val ARCH_LAMP_ITEM = ArchLampItem()

	@JvmField
	val CONJURED_COMPASS_ITEM = ConjuredCompassItem()
	@JvmField
	val GRIMOIRE_ITEM = GrimoireItem()
	@JvmField
	val SCARAB_BEETLE_ITEM = ScarabBeetleItem()
	val TCHOTCHKE_ITEM = TchotchkeItem()
	val HEXBURST_ITEM = HexburstItem()
	val HEXTITO_ITEM = HextitoItem()
	val SMALL_ANIMATED_SCROLL_ITEM = AnimatedScrollItem(1)
	val MEDIUM_ANIMATED_SCROLL_ITEM = AnimatedScrollItem(2)
	val LARGE_ANIMATED_SCROLL_ITEM = AnimatedScrollItem(3)

	private val LEDGER_ITEM = LedgerItem()

	@JvmField
	val LEI = LeiItem()
	private val GAUNTLET_STAFF = ItemStaff(Settings().maxCount(1))
	private val LIGHTNING_ROD_STAFF = ItemStaff(Settings().maxCount(1))

	@JvmStatic
	fun init() {
		Registry.register(Registries.ITEM_GROUP, HEXICAL_GROUP_KEY, HEXICAL_GROUP)

		ItemGroupEvents.MODIFY_ENTRIES_ALL.register { tab, entries ->
			if (tab != HEXICAL_GROUP)
				return@register

			val handLamp = ItemStack(HAND_LAMP_ITEM)
			IXplatAbstractions.INSTANCE.findHexHolder(handLamp)!!.writeHex(listOf(), null, 32000 * MediaConstants.DUST_UNIT)
			entries.add(handLamp)

			val archLamp = ItemStack(ARCH_LAMP_ITEM)
			IXplatAbstractions.INSTANCE.findHexHolder(archLamp)!!.writeHex(listOf(), null, 32000 * MediaConstants.DUST_UNIT)
			entries.add(archLamp)

			entries.add(ItemStack(SMALL_ANIMATED_SCROLL_ITEM))
			entries.add(ItemStack(MEDIUM_ANIMATED_SCROLL_ITEM))
			entries.add(ItemStack(LARGE_ANIMATED_SCROLL_ITEM))

			entries.add(ItemStack(GAUNTLET_STAFF))
			entries.add(ItemStack(LIGHTNING_ROD_STAFF))

			entries.add(ItemStack(LEDGER_ITEM))

			entries.add(ItemStack(LEI))
			entries.add(ItemStack(SCARAB_BEETLE_ITEM))
			entries.add(ItemStack(GRIMOIRE_ITEM))
		}

		Registry.register(Registries.ITEM, HexicalMain.id("animated_scroll_small"), SMALL_ANIMATED_SCROLL_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("animated_scroll_medium"), MEDIUM_ANIMATED_SCROLL_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("animated_scroll_large"), LARGE_ANIMATED_SCROLL_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("hand_lamp"), HAND_LAMP_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("arch_lamp"), ARCH_LAMP_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("conjured_compass"), CONJURED_COMPASS_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("scarab_beetle"), SCARAB_BEETLE_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("grimoire"), GRIMOIRE_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("gauntlet_staff"), GAUNTLET_STAFF)
		Registry.register(Registries.ITEM, HexicalMain.id("lightning_rod_staff"), LIGHTNING_ROD_STAFF)
		Registry.register(Registries.ITEM, HexicalMain.id("hexburst"), HEXBURST_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("hextito"), HEXTITO_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("lei"), LEI)
		Registry.register(Registries.ITEM, HexicalMain.id("tchotchke"), TCHOTCHKE_ITEM)
		Registry.register(Registries.ITEM, HexicalMain.id("ledger"), LEDGER_ITEM)
	}

	@JvmStatic
	fun clientInit() {
		ModelPredicateProviderRegistry.register(ARCH_LAMP_ITEM, Identifier("active")) { stack, _, _, _ -> if (stack.nbt?.getBoolean("active") == true) 1.0f else 0.0f }
		ModelPredicateProviderRegistry.register(SCARAB_BEETLE_ITEM, Identifier("active")) { stack, _, _, _ -> if (stack.nbt?.getBoolean("active") == true) 1.0f else 0.0f }
		ModelPredicateProviderRegistry.register(CONJURED_COMPASS_ITEM, Identifier("angle"), CompassAnglePredicateProvider(CompassTarget { world: ClientWorld, stack: ItemStack, player: Entity ->
			val nbt = stack.nbt ?: return@CompassTarget null
			if (nbt.getString("dimension") != world.dimensionKey.value.toString())
				return@CompassTarget null
			return@CompassTarget GlobalPos.create(player.world.registryKey, BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")))
		}))
	}
}