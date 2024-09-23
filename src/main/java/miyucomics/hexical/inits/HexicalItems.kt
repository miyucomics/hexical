package miyucomics.hexical.inits

import at.petrak.hexcasting.common.items.ItemStaff
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.items.*
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.client.item.CompassAnglePredicateProvider
import net.minecraft.client.item.CompassAnglePredicateProvider.CompassTarget
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos
import net.minecraft.util.registry.Registry

object HexicalItems {
	val HEXICAL_GROUP: ItemGroup = FabricItemGroupBuilder.create(HexicalMain.id("general")).icon { ItemStack(CONJURED_STAFF_ITEM) }.build()

	@JvmField
	val HAND_LAMP_ITEM = HandLampItem()
	@JvmField
	val ARCH_LAMP_ITEM = ArchLampItem()
	val WANDERING_LAMP_ITEM = WanderingLampItem()

	@JvmField
	val CONJURED_COMPASS_ITEM = ConjuredCompassItem()
	val GRIMOIRE_ITEM = GrimoireItem()
	val CONJURED_STAFF_ITEM = ConjuredStaffItem()
	val HEXBURST_ITEM = HexburstItem()
	val HEXTITO_ITEM = HextitoItem()
	val SMALL_LIVING_SCROLL_ITEM = LivingScrollItem(1)
	val MEDIUM_LIVING_SCROLL_ITEM = LivingScrollItem(2)
	val LARGE_LIVING_SCROLL_ITEM = LivingScrollItem(3)

	@JvmStatic
	fun init() {
		Registry.register(Registry.ITEM, HexicalMain.id("lamp"), HAND_LAMP_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("arch_lamp"), ARCH_LAMP_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("wandering_lamp"), WANDERING_LAMP_ITEM)

		Registry.register(Registry.ITEM, HexicalMain.id("gauntlet_staff"), ItemStaff(Settings().maxCount(1).group(HEXICAL_GROUP)))
		Registry.register(Registry.ITEM, HexicalMain.id("lightning_rod_staff"), ItemStaff(Settings().maxCount(1).group(HEXICAL_GROUP)))

		Registry.register(Registry.ITEM, HexicalMain.id("grimoire"), GRIMOIRE_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("living_scroll_small"), SMALL_LIVING_SCROLL_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("living_scroll_medium"), MEDIUM_LIVING_SCROLL_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("living_scroll_large"), LARGE_LIVING_SCROLL_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("conjured_compass"), CONJURED_COMPASS_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("conjured_staff"), CONJURED_STAFF_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("hexburst"), HEXBURST_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("hextito"), HEXTITO_ITEM)
		Registry.register(Registry.ITEM, HexicalMain.id("mage_block"), BlockItem(HexicalBlocks.MAGE_BLOCK, Settings()))
		Registry.register(Registry.ITEM, HexicalMain.id("media_jar"), BlockItem(HexicalBlocks.MEDIA_JAR_BLOCK, Settings().group(HEXICAL_GROUP)))
		Registry.register(Registry.ITEM, HexicalMain.id("hex_candle"), BlockItem(HexicalBlocks.HEX_CANDLE_BLOCK, Settings().group(HEXICAL_GROUP)))
	}

	@JvmStatic
	fun clientInit() {
		ModelPredicateProviderRegistry.register(CONJURED_STAFF_ITEM, Identifier("sprite")) { stack: ItemStack, _: ClientWorld?, _: LivingEntity?, _: Int -> stack.orCreateNbt.getFloat("sprite") / 10 }
		ModelPredicateProviderRegistry.register(CONJURED_COMPASS_ITEM, Identifier("angle"), CompassAnglePredicateProvider(CompassTarget { _: ClientWorld, stack: ItemStack, player: Entity ->
			val nbt = stack.nbt ?: return@CompassTarget null
			return@CompassTarget GlobalPos.create(player.world.registryKey, BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")))
		}))
		ModelPredicateProviderRegistry.register(WANDERING_LAMP_ITEM, Identifier("angle"), CompassAnglePredicateProvider(CompassTarget { _: ClientWorld, stack: ItemStack, player: Entity ->
			val nbt = stack.nbt ?: return@CompassTarget null
			if (!nbt.contains("x"))
				return@CompassTarget null
			return@CompassTarget GlobalPos.create(player.world.registryKey, BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")))
		}))
	}
}