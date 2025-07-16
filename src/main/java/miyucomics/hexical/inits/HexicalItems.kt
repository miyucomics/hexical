package miyucomics.hexical.inits

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.items.ItemStaff
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.animated_scrolls.AnimatedScrollItem
import miyucomics.hexical.features.confection.HexburstItem
import miyucomics.hexical.features.confection.HextitoItem
import miyucomics.hexical.features.grimoires.GrimoireItem
import miyucomics.hexical.features.lamps.ArchLampItem
import miyucomics.hexical.features.lamps.HandLampItem
import miyucomics.hexical.features.lei.LeiItem
import miyucomics.hexical.features.media_jar.MediaJarBlock
import miyucomics.hexical.features.media_log.MediaLogItem
import miyucomics.hexical.features.scarabs.ScarabBeetleItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.item.Item.Settings
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object HexicalItems {
	private val HEXICAL_GROUP_KEY: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, HexicalMain.id("general"))

	private fun <T : Item> registerItem(path: String, item: T): T {
		val id = HexicalMain.id(path)
		Registry.register(Registries.ITEM, id, item)
		return item
	}

	val SMALL_ANIMATED_SCROLL_ITEM = registerItem("animated_scroll_small", AnimatedScrollItem(1))
	val MEDIUM_ANIMATED_SCROLL_ITEM = registerItem("animated_scroll_medium", AnimatedScrollItem(2))
	val LARGE_ANIMATED_SCROLL_ITEM = registerItem("animated_scroll_large", AnimatedScrollItem(3))
	@JvmField val HAND_LAMP_ITEM = registerItem("hand_lamp", HandLampItem())
	@JvmField val ARCH_LAMP_ITEM = registerItem("arch_lamp", ArchLampItem())

	val SCARAB_BEETLE_ITEM = registerItem("scarab_beetle", ScarabBeetleItem())
	@JvmField val GRIMOIRE_ITEM = registerItem("grimoire", GrimoireItem())

	val HEX_GUMMY = registerItem("hex_gummy", Item(Settings().food(FoodComponent.Builder().hunger(2).saturationModifier(0.5f).alwaysEdible().snack().build())))
	val HEXBURST_ITEM = registerItem("hexburst", HexburstItem())
	val HEXTITO_ITEM = registerItem("hextito", HextitoItem())

	private val MEDIA_LOG_ITEM = registerItem("media_log", MediaLogItem())

	@JvmField
	val LEI = registerItem("lei", LeiItem())
	private val GAUNTLET_STAFF = registerItem("gauntlet_staff", ItemStaff(Settings().maxCount(1)))
	private val LIGHTNING_ROD_STAFF = registerItem("lightning_rod_staff", ItemStaff(Settings().maxCount(1)))

	val CURIO_NAMES: List<String> = listOf("bismuth", "clover", "compass", "conch", "cube", "flute", "handbell", "heart", "interlock", "key", "staff", "charm", "strange", "beauty", "truth", "up", "down")
	val CURIOS: List<Item> = CURIO_NAMES.map { registerItem("curio_$it", Item(Settings().maxCount(1))) }
	@JvmField val CURIO_COMPASS = CURIOS[CURIO_NAMES.indexOf("compass")]

	val PLUSHIE_NAMES: List<String> = listOf("hexxy", "irissy", "pentxxy", "quadxxy", "thothy", "flexxy")
	val PLUSHIES: List<Item> = PLUSHIE_NAMES.map { registerItem("plush_$it", Item(Settings().maxCount(1))) }
	@JvmStatic
	fun randomPlush() = ItemStack(PLUSHIES.random())

	val HEXICAL_GROUP: ItemGroup = FabricItemGroup.builder()
		.icon { ItemStack(CURIO_COMPASS) }
		.displayName(Text.translatable("itemGroup.hexical.general"))
		.entries { context, entries ->
			entries.add(ItemStack(HAND_LAMP_ITEM).also { IXplatAbstractions.INSTANCE.findHexHolder(it)!!.writeHex(listOf(), null, 32000 * MediaConstants.DUST_UNIT) })
			entries.add(ItemStack(ARCH_LAMP_ITEM).also { IXplatAbstractions.INSTANCE.findHexHolder(it)!!.writeHex(listOf(), null, 32000 * MediaConstants.DUST_UNIT) })

			entries.add(ItemStack(SMALL_ANIMATED_SCROLL_ITEM))
			entries.add(ItemStack(MEDIUM_ANIMATED_SCROLL_ITEM))
			entries.add(ItemStack(LARGE_ANIMATED_SCROLL_ITEM))

			entries.add(ItemStack(HEX_GUMMY))

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
		registerItem("tchotchke", TchotchkeItem())
	}
}

class TchotchkeItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun isItemBarVisible(stack: ItemStack) = false
	override fun canRecharge(stack: ItemStack) = false
	override fun breakAfterDepletion() = true
	override fun cooldown() = 0

	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		if (world.isClient)
			return TypedActionResult.success(player.getStackInHand(usedHand))
		val stack = player.getStackInHand(usedHand)
		if (hasHex(stack) && getMedia(stack) > 0) {
			val charmed = ItemStack(Items.STICK)
			val nbt = charmed.orCreateNbt
			val charm = NbtCompound()
			charm.putLong("media", getMedia(stack))
			charm.putLong("max_media", getMaxMedia(stack))
			charm.putCompound("instructions", IotaType.serialize(ListIota(getHex(stack, world as ServerWorld)!!)))
			charm.putBoolean("left", true)
			charm.putBoolean("right", true)
			charm.putBoolean("left_sneak", true)
			charm.putBoolean("right_sneak", true)
			nbt.putCompound("charmed", charm)
			player.setStackInHand(usedHand, charmed)
		}
		return TypedActionResult.success(player.getStackInHand(usedHand))
	}

	override fun appendTooltip(stack: ItemStack, world: World?, lines: MutableList<Text>, advanced: TooltipContext) {
		lines.add(Text.literal("Right-click this item to get a charmed stick before tchotchkes go away forever!! Your media will be converted safely.").formatted(Formatting.RED))
	}
}