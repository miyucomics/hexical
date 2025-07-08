package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand

object CharmedItemUtilities {
	@JvmStatic
	fun getUseableCharmedItems(player: PlayerEntity): List<Pair<Hand, ItemStack>> {
		val options = mutableListOf<Pair<Hand, ItemStack>>()
		if (isStackCharmed(player.getStackInHand(Hand.MAIN_HAND))) options.add(Pair(Hand.MAIN_HAND, player.getStackInHand(Hand.MAIN_HAND)))
		if (isStackCharmed(player.getStackInHand(Hand.OFF_HAND))) options.add(Pair(Hand.OFF_HAND, player.getStackInHand(Hand.OFF_HAND)))
		return options
	}

	@JvmStatic
	fun shouldIntercept(stack: ItemStack, button: Int, sneaking: Boolean): Boolean {
		val charmed = stack.nbt!!.getCompound("charmed")
		val key = when (button) {
			0 -> if (sneaking) "left_sneak" else "left"
			1 -> if (sneaking) "right_sneak" else "right"
			else -> return true
		}
		return charmed.getBoolean(key)
	}

	fun isStackCharmed(stack: ItemStack): Boolean {
		return stack.hasNbt() && stack.nbt!!.contains("charmed")
	}

	fun removeCharm(stack: ItemStack) {
		stack.nbt!!.remove("charmed")
	}

	fun getHex(stack: ItemStack, world: ServerWorld): List<Iota> {
		return (IotaType.deserialize(stack.nbt!!.getCompound("charmed").getCompound("instructions"), world) as ListIota).list.toList()
	}

	fun getMedia(stack: ItemStack): Long {
		return stack.nbt!!.getCompound("charmed").getLong("media")
	}

	fun getMaxMedia(stack: ItemStack): Long {
		return stack.nbt!!.getCompound("charmed").getLong("max_media")
	}

	fun deductMedia(stack: ItemStack, cost: Long) {
		val oldMedia = getMedia(stack)
		if (oldMedia <= cost)
			removeCharm(stack)
		else
			stack.nbt!!.getCompound("charmed").putLong("media", oldMedia - cost)
	}
}