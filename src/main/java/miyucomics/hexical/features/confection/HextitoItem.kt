package miyucomics.hexical.features.confection

import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.styledWith
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.world.World

class HextitoItem : Item(Settings().maxCount(16).food(FoodComponent.Builder().alwaysEdible().snack().build())) {
	override fun getMaxUseTime(stack: ItemStack) = 10
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		if (world.isClient)
			return super.finishUsing(stack, world, user)
		if (user !is ServerPlayerEntity)
			return super.finishUsing(stack, world, user)
		val vm = CastingVM(IXplatAbstractions.INSTANCE.getStaffcastVM(user, user.activeHand).image.copy(), HextitoCastEnv(user, Hand.MAIN_HAND))
		if (vm.image.parenCount == 0 && stack.orCreateNbt.contains("hex")) {
			vm.queueExecuteAndWrapIotas(HexSerialization.backwardsCompatibleReadHex(stack.orCreateNbt, "hex", world as ServerWorld), world)
			IXplatAbstractions.INSTANCE.setStaffcastImage(user, vm.image)
		}
		return super.finishUsing(stack, world, user)
	}

	override fun appendTooltip(stack: ItemStack, world: World?, list: MutableList<Text>, context: TooltipContext) {
		list.add("hexical.hextito.hex".asTranslatedComponent(stack.getList("hex", NbtElement.COMPOUND_TYPE.toInt())!!.fold(Text.empty()) { acc, curr -> acc.append(IotaType.getDisplay(curr.asCompound)) }).styledWith(Formatting.GRAY))
	}
}