package miyucomics.hexical.items

import at.petrak.hexcasting.api.item.HexHolderItem
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

class HextitoItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().alwaysEdible().snack().build())), HexHolderItem {
	override fun getMaxUseTime(stack: ItemStack?) = 10
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		if (world.isClient)
			return super.finishUsing(stack, world, user)
		if (user !is ServerPlayerEntity)
			return super.finishUsing(stack, world, user)
		val harness = IXplatAbstractions.INSTANCE.getHarness(user, user.activeHand)
		if (harness.parenCount == 0) {
			var hex = listOf<Iota>()
			if (stack.orCreateNbt.contains("hex"))
				hex = (HexIotaTypes.deserialize(stack.orCreateNbt.getCompound("hex")!!, world as ServerWorld) as ListIota).list.toList()
			harness.executeIotas(hex.toList(), world as ServerWorld)
		}
		IXplatAbstractions.INSTANCE.setHarness(user, harness)
		return super.finishUsing(stack, world, user)
	}

	override fun getMedia(stack: ItemStack?) = 0
	override fun getMaxMedia(stack: ItemStack?) = 0
	override fun setMedia(stack: ItemStack?, media: Int) {}
	override fun canRecharge(stack: ItemStack?) = false
	override fun canProvideMedia(stack: ItemStack?) = false
	override fun canDrawMediaFromInventory(stack: ItemStack?) = true
	override fun hasHex(stack: ItemStack?) = false
	override fun getHex(stack: ItemStack?, level: ServerWorld?) = listOf<Iota>()
	override fun writeHex(stack: ItemStack?, program: MutableList<Iota>?, media: Int) {}
	override fun clearHex(stack: ItemStack?) {}
}