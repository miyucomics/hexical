package miyucomics.hexical.items

import miyucomics.hexical.registry.HexicalItems
import net.minecraft.client.item.CompassAnglePredicateProvider
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos

class ConjuredCompassItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().hunger(2).alwaysEdible().snack().build())) {
	override fun getMaxUseTime(stack: ItemStack) = 40

	companion object {
		fun registerModelPredicate() {
			ModelPredicateProviderRegistry.register(HexicalItems.CONJURED_COMPASS_ITEM, Identifier("angle"), CompassAnglePredicateProvider(
				CompassAnglePredicateProvider.CompassTarget { world: ClientWorld, stack: ItemStack, player: Entity ->
					val nbt = stack.nbt ?: return@CompassTarget null
					if (nbt.getString("dimension") != world.dimensionKey.value.toString())
						return@CompassTarget null
					return@CompassTarget GlobalPos.create(
						player.world.registryKey,
						BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"))
					)
				}
			))
		}
	}
}