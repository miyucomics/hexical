package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.interfaces.PlayerEntityMixinInterface
import miyucomics.hexical.persistent_state.PersistentStateHandler
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalSounds
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class ArchLampItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun use(world: World, user: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(usedHand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)

		val stackNbt = stack.orCreateNbt
		if (!stackNbt.contains("active"))
			stackNbt.putBoolean("active", false)

		if (world.isClient) {
			world.playSound(user.x, user.y, user.z, if (stackNbt.getBoolean("active")) HexicalSounds.LAMP_DEACTIVATE_SOUND_EVENT else HexicalSounds.LAMP_ACTIVATE_SOUND_EVENT, SoundCategory.MASTER, 1f, 1f, true)
			return TypedActionResult.success(stack)
		}

		if (stackNbt.getBoolean("active")) {
			stackNbt.putBoolean("active", false)
			return TypedActionResult.success(stack)
		}

		stackNbt.putBoolean("active", true)

		val state = PersistentStateHandler.getPlayerState(user)
		state.position = user.eyePos
		state.rotation = user.rotationVector
		state.velocity = user.velocity
		state.storage = HexIotaTypes.serialize(NullIota())
		state.time = world.time

		return TypedActionResult.success(stack)
	}

	override fun inventoryTick(stack: ItemStack, world: World, user: Entity, slot: Int, selected: Boolean) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return
		if (user !is ServerPlayerEntity) return
		if (!stack.orCreateNbt.getBoolean("active")) return

		if ((user as PlayerEntityMixinInterface).archLampCastedThisTick) {
			for (itemSlot in user.inventory.main)
				if (itemSlot.item == HexicalItems.ARCH_LAMP_ITEM)
					itemSlot.orCreateNbt.putBoolean("active", false)
			user.itemCooldownManager[this] = 100
			return
		}

		CastingUtils.castInvisibly(world as ServerWorld, user, getHex(stack, world) ?: return, true)
		(user as PlayerEntityMixinInterface).lampCastedThisTick()
		if (getMedia(stack) == 0)
			user.inventory.setStack(slot, ItemStack(HexicalItems.LAMP_ITEM))
	}

	override fun breakAfterDepletion(): Boolean { return false }
	override fun canDrawMediaFromInventory(stack: ItemStack): Boolean { return false }
}