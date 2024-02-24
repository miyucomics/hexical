package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.interfaces.CastingContextMixinInterface
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
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

	@Suppress("CAST_NEVER_SUCCEEDS")
	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return
		val hex = getHex(stack, world as ServerWorld) ?: return
		val context = CastingContext((user as ServerPlayerEntity), user.activeHand, CastingContext.CastSource.PACKAGED_HEX)
		(context as CastingContextMixinInterface).setCastByLamp(true)
		val harness = CastingHarness(context)
		harness.executeIotas(hex, world)
	}

	override fun getUseAction(pStack: ItemStack): UseAction { return UseAction.BOW }
	override fun getMaxUseTime(stack: ItemStack): Int { return 72000 }
	override fun breakAfterDepletion(): Boolean { return false }
	override fun canDrawMediaFromInventory(stack: ItemStack): Boolean { return false }
}