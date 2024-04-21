package miyucomics.hexical.items

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.state.PersistentStateHandler
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalSounds
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

class ArchLampItem : ItemPackagedHex(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)) {
	override fun appendStacks(group: ItemGroup?, stacks: DefaultedList<ItemStack>?) {
		if (this.isIn(group)) {
			val archLampStack = ItemStack(HexicalItems.ARCH_LAMP_ITEM)
			val archLampHexHolder = IXplatAbstractions.INSTANCE.findHexHolder(archLampStack)
			archLampHexHolder!!.writeHex(listOf(), MediaConstants.DUST_UNIT * 64000)
			stacks!!.add(archLampStack)
		}
	}

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
			lampCast(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world)!!, archLamp = true, finale = true)
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

		if ((user as PlayerEntityMinterface).getArchLampCastedThisTick()) {
			for (itemSlot in user.inventory.main)
				if (itemSlot.item == HexicalItems.ARCH_LAMP_ITEM)
					itemSlot.orCreateNbt.putBoolean("active", false)
			user.itemCooldownManager[this] = 100
			return
		}

		lampCast(world as ServerWorld, user, getHex(stack, world) ?: return, archLamp = true, finale = false)
		(user as PlayerEntityMinterface).lampCastedThisTick()
		if (getMedia(stack) == 0) {
			user.inventory.setStack(slot, ItemStack(HexicalItems.LAMP_ITEM))
			HexicalAdvancements.USE_UP_LAMP.trigger(user)
		}
	}

	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun canRecharge(stack: ItemStack?) = false
	override fun breakAfterDepletion() = false
}

fun hasActiveArchLamp(player: ServerPlayerEntity): Boolean {
	for (stack in player.inventory.main)
		if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
			return true
	for (stack in player.inventory.offHand)
		if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
			return true
	return false
}