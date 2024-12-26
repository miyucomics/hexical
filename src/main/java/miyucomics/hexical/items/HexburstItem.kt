package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

class HexburstItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().alwaysEdible().snack().build())) {
	override fun getMaxUseTime(stack: ItemStack) = 10
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		if (world.isClient)
			return super.finishUsing(stack, world, user)
		if (user !is ServerPlayerEntity)
			return super.finishUsing(stack, world, user)
		val image = IXplatAbstractions.INSTANCE.getStaffcastVM(user, user.activeHand).image
		val newStack = image.stack.toMutableList()
		newStack.add(if (stack.orCreateNbt.contains("iota")) IotaType.deserialize(stack.orCreateNbt.getCompound("iota")!!, world as ServerWorld) else GarbageIota())
		IXplatAbstractions.INSTANCE.setStaffcastImage(user, image.copy(stack = newStack))
		return super.finishUsing(stack, world, user)
	}
}