package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World


class LampItem : ItemPackagedHex(Settings()) {
	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(usedHand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)

		if (!world.isClient) {
			stack.nbt?.putLongArray("startPosition", player.eyePos.serializeToNBT().longArray);
			stack.nbt?.putLongArray("startRotation", player.rotationVector.serializeToNBT().longArray);
			stack.nbt?.putLong("startTime", world.time);
		}

		player.setCurrentHand(usedHand)
		return TypedActionResult.success(stack)
	}

	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return

		val hex = getHex(stack, world as ServerWorld) ?: return
		val harness = CastingHarness(CastingContext((user as ServerPlayerEntity), user.getActiveHand(), CastingContext.CastSource.PACKAGED_HEX))
		harness.executeIotas(hex, world)
	}

	override fun getUseAction(pStack: ItemStack): UseAction { return UseAction.BOW }
	override fun getMaxUseTime(stack: ItemStack): Int { return 72000 }
	override fun breakAfterDepletion(): Boolean { return false }
	override fun canDrawMediaFromInventory(stack: ItemStack): Boolean { return false }

	companion object {
		@JvmStatic
		fun isUsingLamp(context: CastingContext): Boolean {
			return context.source == CastingContext.CastSource.PACKAGED_HEX && (context.caster as PlayerEntity).activeItem.item == HexicalItems.LAMP_ITEM
		}
	}
}