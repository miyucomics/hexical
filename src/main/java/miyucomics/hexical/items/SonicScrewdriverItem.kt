package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.casting.environments.SonicScrewdriverCastEnv
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import kotlin.math.min

class SonicScrewdriverItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack?> {
		user.setCurrentHand(hand)
		return TypedActionResult.consume(user.getStackInHand(hand))
	}

	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (!hasHex(stack)) return
		if (world.isClient) return

		val useTime = getMaxUseTime(stack) - remainingUseTicks
		if (useTime < 10)
			return

		if (itemOperates(world as ServerWorld, user, stack))
			user.stopUsingItem()
	}

	private fun itemOperates(world: ServerWorld, user: LivingEntity, stack: ItemStack): Boolean {
		val vm = CastingVM(CastingImage(), SonicScrewdriverCastEnv(user as ServerPlayerEntity, user.activeHand, stack))
		vm.queueExecuteAndWrapIotas(getHex(stack, world)!!, world)
		return false
	}

	override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, i: Int, bl: Boolean) {
		setMedia(stack, min(getMedia(stack) + MediaConstants.DUST_UNIT / 200, MediaConstants.DUST_UNIT))
	}

	override fun cooldown() = 0
	override fun breakAfterDepletion() = false
	override fun getMaxUseTime(stack: ItemStack) = 72000
	override fun getUseAction(stack: ItemStack) = UseAction.BOW
	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun getMaxMedia(stack: ItemStack) = MediaConstants.DUST_UNIT
}