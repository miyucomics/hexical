package miyucomics.hexical.items

import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.World

class MemoryBerriesItem : Item(Settings().maxCount(64).group(HexicalItems.HEXICAL_GROUP).food(FoodComponent.Builder().alwaysEdible().snack().build())) {
	override fun getMaxUseTime(stack: ItemStack?) = 10
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		if (world.isClient)
			return super.finishUsing(stack, world, user)
		if (user !is ServerPlayerEntity)
			return super.finishUsing(stack, world, user)
		val harness = IXplatAbstractions.INSTANCE.getHarness(user, user.activeHand)
		if (harness.parenCount > 0) {
			val parenthesized = harness.parenthesized.toMutableList()
			parenthesized.removeLast()
			harness.parenthesized = parenthesized
			IXplatAbstractions.INSTANCE.setHarness(user, harness)

			val resolvedPatterns = IXplatAbstractions.INSTANCE.getPatterns(user).toMutableList()
			resolvedPatterns.removeLast()
			IXplatAbstractions.INSTANCE.setPatterns(user, resolvedPatterns)
		}
		return super.finishUsing(stack, world, user)
	}
}