package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.interfaces.CastingContextMixinInterface
import miyucomics.hexical.persistent_state.StateHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class MasterLampItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(usedHand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)
		if (world.isClient) return TypedActionResult.success(stack)

		val state = StateHandler.getPlayerState(player)
		val stackNbt = stack.orCreateNbt
		if (!stackNbt.contains("active"))
			stackNbt.putBoolean("active", false)

		if (state.active && !stackNbt.getBoolean("active")) {
			player.sendMessage(Text.literal("You're lucky I haven't implemented a mishap yet."))
			return TypedActionResult.fail(stack)
		}

		if (state.active) {
			state.active = false
			stackNbt.putBoolean("active", false)
			return TypedActionResult.success(stack)
		}

		state.active = true
		stackNbt.putBoolean("active", true)
		state.startPosition = player.eyePos
		state.startRotation = player.rotationVector
		state.startTime = world.time
		player.sendMessage(Text.literal("Activated!"))

		return TypedActionResult.success(stack)
	}

	@Suppress("CAST_NEVER_SUCCEEDS")
	override fun inventoryTick(stack: ItemStack, world: World, user: Entity, slot: Int, selected: Boolean) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return
		if (!stack.orCreateNbt.getBoolean("active")) return
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