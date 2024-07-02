package miyucomics.hexical.items

import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class ConjuredCompassItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().hunger(2).alwaysEdible().snack().build())) {
	override fun getMaxUseTime(stack: ItemStack?) = 40
}