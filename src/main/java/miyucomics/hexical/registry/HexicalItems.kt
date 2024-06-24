package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.items.*
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.client.item.CompassAnglePredicateProvider
import net.minecraft.client.item.CompassAnglePredicateProvider.CompassTarget
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.item.*
import net.minecraft.item.Item.Settings
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos
import net.minecraft.util.registry.Registry

object HexicalItems {
	val HEXICAL_GROUP: ItemGroup = FabricItemGroupBuilder.create(HexicalMain.id("general")).icon { ItemStack(CONJURED_STAFF_ITEM) }.build()

	val LAMP_ITEM: LampItem = LampItem()
	val ARCH_LAMP_ITEM: ArchLampItem = ArchLampItem()
	val CONJURED_COMPASS_ITEM = Item(Settings())
	val GRIMOIRE_ITEM: GrimoireItem = GrimoireItem()
	val CONJURED_STAFF_ITEM: ConjuredStaffItem = ConjuredStaffItem()
	val HEXBURST_ITEM: HexburstItem = HexburstItem()
	val HEXTITO_ITEM: HextitoItem = HextitoItem()
	val NULL_MEDIA_ITEM: NullMediaItem = NullMediaItem()

	@JvmStatic
	fun init() {
		Registry.register(Registry.ITEM, HexicalMain.id("grimoire"), GRIMOIRE_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("lamp"), LAMP_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("arch_lamp"), ARCH_LAMP_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("living_scroll_small"), LivingScrollItem(1))
		Registry.register(Registry.ITEM, HexicalMain.id("living_scroll_medium"), LivingScrollItem(2))
		Registry.register(Registry.ITEM, HexicalMain.id("living_scroll_large"), LivingScrollItem(3))
		Registry.register(Registry.ITEM, HexicalMain.id("lightning_rod_staff"), LightningRodStaff())
		Registry.register(Registry.ITEM, HexicalMain.id("conjured_staff"), CONJURED_STAFF_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("conjured_compass"), CONJURED_COMPASS_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("hexburst"), HEXBURST_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("hextito"), HEXTITO_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("null_media"), NULL_MEDIA_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("mage_block"), BlockItem(HexicalBlocks.MAGE_BLOCK, Settings()))
	}

	@JvmStatic
	fun clientInit() {
		ModelPredicateProviderRegistry.register(CONJURED_COMPASS_ITEM, Identifier("angle"), CompassAnglePredicateProvider(CompassTarget { _: ClientWorld?, stack: ItemStack?, player: Entity? ->
			if (player != null && stack != null && stack.hasNbt()) {
				val nbt = stack.nbt!!
				return@CompassTarget GlobalPos.create(player.world.registryKey, BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")))
			}
			null
		}))
	}
}