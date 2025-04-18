package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.item.VariantItem
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.casting.environments.TchotchkeCastEnv
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class TchotchkeItem : ItemPackagedHex(Settings().maxCount(1)), VariantItem {
	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> = TypedActionResult.success(player.getStackInHand(usedHand))
	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun isItemBarVisible(stack: ItemStack) = false
	override fun canRecharge(stack: ItemStack) = false
	override fun breakAfterDepletion() = true
	override fun numVariants() = 10
	override fun cooldown() = 0

	override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
		if (!hasHex(stack) || getMedia(stack) == 0L)
			stack.decrement(1)
	}

	companion object {
		fun cast(user: ServerPlayerEntity, hand: Hand, itemStack: ItemStack, input: Boolean) {
			val vm = CastingVM(CastingImage().copy(stack = listOf(BooleanIota(input))), TchotchkeCastEnv(user, hand, itemStack))
			vm.queueExecuteAndWrapIotas((itemStack.item as TchotchkeItem).getHex(itemStack, user.serverWorld)!!, user.serverWorld)
		}
	}
}

fun getTchotchke(player: PlayerEntity): Hand? {
	if (player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.TCHOTCHKE_ITEM))
		return Hand.OFF_HAND
	if (player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.TCHOTCHKE_ITEM))
		return Hand.MAIN_HAND
	return null
}