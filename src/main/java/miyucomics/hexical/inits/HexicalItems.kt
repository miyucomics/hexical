package miyucomics.hexical.inits

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.items.ItemStaff
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.animated_scrolls.AnimatedScrollItem
import miyucomics.hexical.features.confection.HexburstItem
import miyucomics.hexical.features.confection.HextitoItem
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.features.driver_dots.DriverDotItem
import miyucomics.hexical.features.grimoires.GrimoireItem
import miyucomics.hexical.features.lamps.ArchLampItem
import miyucomics.hexical.features.lamps.HandLampItem
import miyucomics.hexical.features.media_jar.MediaJarBlock
import miyucomics.hexical.features.media_log.MediaLogItem
import miyucomics.hexical.features.periwinkle.LeiItem
import miyucomics.hexical.features.scarabs.ScarabBeetleItem
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

	val SMALL_ANIMATED_SCROLL_ITEM = registerItem("animated_scroll_small", AnimatedScrollItem(1))
	val MEDIUM_ANIMATED_SCROLL_ITEM = registerItem("animated_scroll_medium", AnimatedScrollItem(2))
	val LARGE_ANIMATED_SCROLL_ITEM = registerItem("animated_scroll_large", AnimatedScrollItem(3))
	@JvmField val HAND_LAMP_ITEM = registerItem("hand_lamp", HandLampItem)
	@JvmField val ARCH_LAMP_ITEM = registerItem("arch_lamp", ArchLampItem)

	val DRIVER_DOT_ITEM = registerItem("driver_dot", DriverDotItem)
	@JvmField val GRIMOIRE_ITEM = registerItem("grimoire", GrimoireItem)
	val SCARAB_BEETLE_ITEM = registerItem("scarab_beetle", ScarabBeetleItem)

	val HEX_GUMMY = registerItem("hex_gummy", Item(Settings().food(FoodComponent.Builder().hunger(2).saturationModifier(0.5f).alwaysEdible().snack().build())))
	@JvmField val HEXBURST_ITEM = registerItem("hexburst", HexburstItem)
	val HEXTITO_ITEM = registerItem("hextito", HextitoItem)

	private val MEDIA_LOG_ITEM = registerItem("media_log", MediaLogItem)

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

	val PLUSHIE_NAMES: List<String> = listOf("hexxy", "irissy", "pentxxy", "quadxxy", "thothy", "flexxy")
	val PLUSHIES: List<Item> = PLUSHIE_NAMES.map { registerItem("plush_$it", Item(Settings().maxCount(1))) }
	@JvmStatic
	fun randomPlush() = ItemStack(PLUSHIES.random())

	val HEXICAL_GROUP: ItemGroup = FabricItemGroup.builder()
		.icon { ItemStack(CURIO_COMPASS) }
		.displayName(Text.translatable("itemGroup.hexical.general"))
		.entries { _, entries ->
			entries.add(ItemStack(HAND_LAMP_ITEM).also { IXplatAbstractions.INSTANCE.findHexHolder(it)!!.writeHex(listOf(), null, 32000 * MediaConstants.DUST_UNIT) })
			entries.add(ItemStack(ARCH_LAMP_ITEM).also { IXplatAbstractions.INSTANCE.findHexHolder(it)!!.writeHex(listOf(), null, 32000 * MediaConstants.DUST_UNIT) })

			entries.add(ItemStack(SMALL_ANIMATED_SCROLL_ITEM))
			entries.add(ItemStack(MEDIUM_ANIMATED_SCROLL_ITEM))
			entries.add(ItemStack(LARGE_ANIMATED_SCROLL_ITEM))

			entries.add(ItemStack(HEX_GUMMY))

			entries.add(ItemStack(DRIVER_DOT_ITEM))
			entries.add(ItemStack(GRIMOIRE_ITEM))
			entries.add(ItemStack(SCARAB_BEETLE_ITEM))
			entries.add(ItemStack(LEI))

			entries.add(ItemStack(MEDIA_LOG_ITEM))
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
			entries.add(ItemStack(HexicalBlocks.PEDESTAL_ITEM))

			for (item in CURIOS)
				entries.add(item)
			for (item in PLUSHIES)
				entries.add(item)
		}
		.build()

	fun init() {
		Registry.register(Registries.ITEM_GROUP, HEXICAL_GROUP_KEY, HEXICAL_GROUP)
	}
}