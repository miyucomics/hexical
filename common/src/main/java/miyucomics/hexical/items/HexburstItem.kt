package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.iota.GarbageIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

class HexburstItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().alwaysEdible().snack().build())) {
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		if (world.isClient)
			return super.finishUsing(stack, world, user)
		if (user !is ServerPlayerEntity)
			return super.finishUsing(stack, world, user)
		val harness = IXplatAbstractions.INSTANCE.getHarness(user, user.activeHand)
		harness.stack.add(if (stack.orCreateNbt.contains("iota")) HexIotaTypes.deserialize(stack.orCreateNbt.getCompound("iota")!!, world as ServerWorld) else GarbageIota())
		IXplatAbstractions.INSTANCE.setHarness(user, harness);
		return super.finishUsing(stack, world, user)
	}
}