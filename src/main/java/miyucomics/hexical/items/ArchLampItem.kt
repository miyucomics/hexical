package miyucomics.hexical.items

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.GenieLamp
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalSounds
import miyucomics.hexical.state.PersistentStateHandler
import miyucomics.hexical.utils.CastingUtils
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

class ArchLampItem : ItemPackagedHex(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)), GenieLamp {
	override fun appendStacks(group: ItemGroup, stacks: DefaultedList<ItemStack>) {
		if (this.isIn(group)) {
			val stack = ItemStack(HexicalItems.ARCH_LAMP_ITEM)
			val holder = IXplatAbstractions.INSTANCE.findHexHolder(stack)
			holder!!.writeHex(listOf(), MediaConstants.DUST_UNIT * 200000)
			stacks.add(stack)
		}
	}

	override fun use(world: World, user: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(usedHand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)

		val stackNbt = stack.orCreateNbt
		if (!stackNbt.contains("active"))
			stackNbt.putBoolean("active", false)

		if (world.isClient) {
			world.playSound(user.x, user.y, user.z, if (stackNbt.getBoolean("active")) HexicalSounds.LAMP_DEACTIVATE else HexicalSounds.LAMP_ACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
			return TypedActionResult.success(stack)
		}

		if (stackNbt.getBoolean("active")) {
			CastingUtils.castSpecial(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world)!!, SpecializedSource.ARCH_LAMP, finale = true)
			stackNbt.putBoolean("active", false)
			return TypedActionResult.success(stack)
		}

		stackNbt.putBoolean("active", true)

		val state = PersistentStateHandler.getPlayerArchLampData(user as ServerPlayerEntity)
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

		CastingUtils.castSpecial(world as ServerWorld, user, getHex(stack, world) ?: return, SpecializedSource.ARCH_LAMP, finale = false)
		(user as PlayerEntityMinterface).archLampCasted()
	}

	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun canRecharge(stack: ItemStack) = false
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