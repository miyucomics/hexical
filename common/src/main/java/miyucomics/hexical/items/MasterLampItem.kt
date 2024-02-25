package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.persistent_state.PersistentStateHandler
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class MasterLampItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(usedHand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)
		if (world.isClient) return TypedActionResult.success(stack)

		val state = PersistentStateHandler.getPlayerState(player)
		val stackNbt = stack.orCreateNbt
		if (!stackNbt.contains("active"))
			stackNbt.putBoolean("active", false)

		val currentlyUsingArchLamp = CastingUtils.doesPlayerHaveActiveArchLamp(player as ServerPlayerEntity)
		if (currentlyUsingArchLamp) {
			if (stackNbt.getBoolean("active")) {
				stackNbt.putBoolean("active", false)
				return TypedActionResult.success(stack)
			} else {
				player.sendMessage(Text.literal("You're lucky I haven't implemented a mishap yet."))
				return TypedActionResult.fail(stack)
			}
		}

		stackNbt.putBoolean("active", true)
		state.position = player.eyePos
		state.rotation = player.rotationVector
		state.velocity = player.velocity
		state.storage = HexIotaTypes.serialize(NullIota())
		state.time = world.time

		return TypedActionResult.success(stack)
	}

	override fun inventoryTick(stack: ItemStack, world: World, user: Entity, slot: Int, selected: Boolean) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return
		if (user !is ServerPlayerEntity) return
		if (!stack.orCreateNbt.getBoolean("active")) return
		CastingUtils.castInvisibly(world as ServerWorld, user, getHex(stack, world) ?: return)
		if (getMedia(stack) == 0) {
			stack.orCreateNbt.putBoolean("active", false)
			world.playSound(user.x, user.y, user.z, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.MASTER, 1f, 1f, true)
			user.inventory.setStack(slot, ItemStack(HexicalItems.TARNISHED_LAMP_ITEM))
		}
	}

	override fun breakAfterDepletion(): Boolean { return false }
	override fun canDrawMediaFromInventory(stack: ItemStack): Boolean { return false }
}