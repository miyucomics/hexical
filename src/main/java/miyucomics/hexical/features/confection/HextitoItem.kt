package miyucomics.hexical.features.confection

import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.environments.HextitoCastEnv
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.world.World

class HextitoItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().alwaysEdible().snack().build())) {
	override fun getMaxUseTime(stack: ItemStack) = 10
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		if (world.isClient)
			return super.finishUsing(stack, world, user)
		if (user !is ServerPlayerEntity)
			return super.finishUsing(stack, world, user)
		val newVM = CastingVM(IXplatAbstractions.INSTANCE.getStaffcastVM(user, user.activeHand).image.copy(), HextitoCastEnv(user, Hand.MAIN_HAND))
		if (newVM.image.parenCount == 0 && stack.orCreateNbt.contains("hex")) {
			val deserialized = IotaType.deserialize(stack.orCreateNbt.getCompound("hex")!!, world as ServerWorld)
			if (deserialized is ListIota)
				newVM.queueExecuteAndWrapIotas(deserialized.list.toList(), world)
		}
		IXplatAbstractions.INSTANCE.setStaffcastImage(user, newVM.image)
		return super.finishUsing(stack, world, user)
	}
}