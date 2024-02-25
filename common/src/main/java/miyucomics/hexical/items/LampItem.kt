package miyucomics.hexical.items

import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class LampItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(usedHand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)
		val stackNbt = stack.orCreateNbt
		if (!world.isClient) {
			stackNbt.putLongArray("position", player.eyePos.serializeToNBT().longArray);
			stackNbt.putLongArray("rotation", player.rotationVector.serializeToNBT().longArray);
			stackNbt.putLongArray("velocity", player.velocity.serializeToNBT().longArray);
			stackNbt.putLong("start_time", world.time);
		}
		player.setCurrentHand(usedHand)
		return TypedActionResult.success(stack)
	}

	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return
		CastingUtils.castInvisibly(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world) ?: return)
		if (getMedia(stack) == 0) {
			world.playSound(user.x, user.y, user.z, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.MASTER, 1f, 1f, true)
			user.setStackInHand(user.activeHand, ItemStack(HexicalItems.TARNISHED_LAMP_ITEM))
		}
	}

	override fun getUseAction(pStack: ItemStack): UseAction { return UseAction.BOW }
	override fun getMaxUseTime(stack: ItemStack): Int { return 72000 }
	override fun breakAfterDepletion(): Boolean { return false }
	override fun canDrawMediaFromInventory(stack: ItemStack): Boolean { return false }
}