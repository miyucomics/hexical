package miyucomics.hexical.features.items

import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.IotaType
import miyucomics.hexical.utils.CastingUtils
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
		CastingUtils.giveIota(user, if (stack.orCreateNbt.contains("iota"))
			IotaType.deserialize(stack.orCreateNbt.getCompound("iota")!!, world as ServerWorld)
		else
			GarbageIota())
		return super.finishUsing(stack, world, user)
	}
}