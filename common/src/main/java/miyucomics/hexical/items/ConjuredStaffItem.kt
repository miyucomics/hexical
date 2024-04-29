package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class ConjuredStaffItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> = TypedActionResult.success(player.getStackInHand(usedHand))
	override fun canDrawMediaFromInventory(stack: ItemStack?) = false
	override fun canRecharge(stack: ItemStack?) = false
	override fun breakAfterDepletion() = true

	override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
		if (!hasHex(stack) || getMedia(stack) == 0)
			stack.decrement(1)
	}

	fun cast(world: ServerWorld, user: ServerPlayerEntity, hand: Hand, stack: ItemStack, castStack: MutableList<Iota>) {
		val context = CastingContext(user, hand, CastingContext.CastSource.PACKAGED_HEX)
		(context as CastingContextMinterface).setSpecializedSource(SpecializedSource.CONJURED_STAFF)
		val harness = CastingHarness(context)
		harness.stack = castStack
		harness.executeIotas(getHex(stack, world)!!, world)
	}
}

fun getConjuredStaff(player: PlayerEntity): Hand? {
	if (player.getStackInHand(Hand.OFF_HAND).item is ConjuredStaffItem)
		return Hand.OFF_HAND
	if (player.getStackInHand(Hand.MAIN_HAND).item is ConjuredStaffItem)
		return Hand.MAIN_HAND
	return null
}