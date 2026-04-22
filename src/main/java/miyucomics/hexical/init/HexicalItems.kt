package miyucomics.hexical.init

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.items.ItemStaff
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.feature.babelbug.BabelbugItem
import miyucomics.hexical.feature.confection.HexburstItem
import miyucomics.hexical.feature.confection.HextitoItem
import miyucomics.hexical.feature.curio.CurioItem
import miyucomics.hexical.feature.driver_dot.DriverDotItem
import miyucomics.hexical.feature.genie_lamp.ArchLampItem
import miyucomics.hexical.feature.genie_lamp.HandLampItem
import miyucomics.hexical.feature.grimoire.GrimoireItem
import miyucomics.hexical.feature.media_jar.MediaJarBlock
import miyucomics.hexical.feature.periwinkle.LeiItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text

object HexicalItems {
	private val HEXICAL_GROUP_KEY: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, HexicalMain.id("general"))
	val CAN_NOT_MAGE_MOUTH_TAG: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, HexicalMain.id("can_not_mage_mouth"))

	private fun <T : Item> registerItem(path: String, item: T): T {
		val id = HexicalMain.id(path)
		Registry.register(Registries.ITEM, id, item)
		return item
	}

	@JvmField val HAND_LAMP_ITEM = registerItem("hand_lamp", HandLampItem)
	@JvmField val ARCH_LAMP_ITEM = registerItem("arch_lamp", ArchLampItem)

	val GRIMOIRE_ITEM = registerItem("grimoire", GrimoireItem)
	val COPPER_DRIVER_DOT_ITEM = registerItem("copper_driver_dot", DriverDotItem(true))
	val IRON_DRIVER_DOT_ITEM = registerItem("iron_driver_dot", DriverDotItem(true))
	val GOLD_DRIVER_DOT_ITEM = registerItem("gold_driver_dot", DriverDotItem(true))
	val NETHERITE_DRIVER_DOT_ITEM = registerItem("netherite_driver_dot", DriverDotItem(false))

	val BABELBUG_ITEM = registerItem("babelbug", BabelbugItem)

	val HEX_GUMMY = registerItem("hex_gummy", Item(Settings().food(FoodComponent.Builder().hunger(2).saturationModifier(0.5f).alwaysEdible().snack().build())))
	@JvmField val HEXBURST_ITEM = registerItem("hexburst", HexburstItem)
	val HEXTITO_ITEM = registerItem("hextito", HextitoItem)

	@JvmField
	val LEI = registerItem("lei", LeiItem)
	private val GAUNTLET_STAFF = registerItem("gauntlet_staff", ItemStaff(Settings().maxCount(1)))
	private val LIGHTNING_ROD_STAFF = registerItem("lightning_rod_staff", ItemStaff(Settings().maxCount(1)))

	val CURIO_NAMES: List<String> = listOf("bismuth", "clover", "compass", "conch", "cube", "flute", "handbell", "heart", "interlock", "key", "staff", "charm", "strange", "beauty", "truth", "up", "down")
	val CURIOS: List<Item> = CURIO_NAMES.map { registerItem("curio_$it", CurioItem.getCurioFromName(it)) }
	@JvmField val CURIO_COMPASS = CURIOS[CURIO_NAMES.indexOf("compass")]
	@JvmField val CURIO_FLUTE = CURIOS[CURIO_NAMES.indexOf("flute")]
	@JvmField val CURIO_HANDBELL = CURIOS[CURIO_NAMES.indexOf("handbell")]
	@JvmField val CURIO_STAFF = CURIOS[CURIO_NAMES.indexOf("staff")]

	val PLUSHIE_NAMES: List<String> = listOf("hexxy", "irissy", "trixxy", "pentxxy", "quadxxy", "thothy", "flexxy")
	val PLUSHIES: List<Item> = PLUSHIE_NAMES.map { registerItem("plush_$it", Item(Settings().maxCount(1))) }
	@JvmStatic
	fun randomPlush() = ItemStack(PLUSHIES.random())

	val HEXICAL_GROUP: ItemGroup = FabricItemGroup.builder()
		.icon { ItemStack(CURIO_COMPASS) }
		.displayName(Text.translatable("itemGroup.hexical.general"))
		.entries { _, entries ->
			entries.add(ItemStack(HAND_LAMP_ITEM))
			entries.add(ItemStack(ARCH_LAMP_ITEM))

			entries.add(ItemStack(HEX_GUMMY))

			entries.add(ItemStack(GRIMOIRE_ITEM))
			entries.add(ItemStack(COPPER_DRIVER_DOT_ITEM))
			entries.add(ItemStack(IRON_DRIVER_DOT_ITEM))
			entries.add(ItemStack(GOLD_DRIVER_DOT_ITEM))
			entries.add(ItemStack(NETHERITE_DRIVER_DOT_ITEM))

			entries.add(ItemStack(BABELBUG_ITEM))

			entries.add(ItemStack(LEI))
			entries.add(ItemStack(GAUNTLET_STAFF))
			entries.add(ItemStack(LIGHTNING_ROD_STAFF))

			entries.add(ItemStack(HexicalBlocks.MEDIA_JAR_ITEM).also {
				val compound = NbtCompound()
				compound.putLong("media", MediaJarBlock.MAX_CAPACITY)
				it.orCreateNbt.putCompound("BlockEntityTag", compound)
			})
			entries.add(ItemStack(HexicalBlocks.HEX_CANDLE_ITEM))
			entries.add(ItemStack(HexicalBlocks.CASTING_CARPET_ITEM))
			entries.add(ItemStack(HexicalBlocks.SENTINEL_BED_ITEM))
			entries.add(ItemStack(HexicalBlocks.PERIWINKLE_FLOWER_ITEM))

			CURIOS.forEach(entries::add)
			PLUSHIES.forEach(entries::add)
			entries.add(ItemStack(HexicalBlocks.PEDESTAL_ITEM))
			HexicalBlocks.PEDESTAL_BLOCK_ITEMS.forEach(entries::add)
		}
		.build()

	fun init() {
		Registry.register(Registries.ITEM_GROUP, HEXICAL_GROUP_KEY, HEXICAL_GROUP)
	}
}