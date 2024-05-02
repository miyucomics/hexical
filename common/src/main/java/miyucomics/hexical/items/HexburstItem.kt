package miyucomics.hexical.items

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class HexburstItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().alwaysEdible().snack().build())) {
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		return super.finishUsing(stack, world, user)
	}
}