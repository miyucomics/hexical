package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.misc.RenderUtils
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Hand

object CharmedItemUtilities {
	val CHARMED_COLOR: TextColor = TextColor.fromRgb(0xe83d72)

	@JvmStatic
	fun getUseableCharmedItems(player: PlayerEntity): List<Pair<Hand, ItemStack>> {
		val options = mutableListOf<Pair<Hand, ItemStack>>()
		if (isStackCharmed(player.getStackInHand(Hand.MAIN_HAND))) options.add(Pair(
			Hand.MAIN_HAND, player.getStackInHand(
				Hand.MAIN_HAND)))
		if (isStackCharmed(player.getStackInHand(Hand.OFF_HAND))) options.add(Pair(
			Hand.OFF_HAND, player.getStackInHand(
				Hand.OFF_HAND)))
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

	fun removeCharm(stack: ItemStack) {
		stack.nbt!!.remove("charmed")
		if (stack.nbt!!.isEmpty)
			stack.nbt = null
	}

	fun deductMedia(stack: ItemStack, cost: Long) {
		val oldMedia = getMedia(stack)
		if (oldMedia <= cost)
			removeCharm(stack)
		else
			getCompound(stack).putLong("media", oldMedia - cost)
	}

	fun isStackCharmed(stack: ItemStack) = stack.hasNbt() && stack.nbt!!.contains("charmed")
	fun getCompound(stack: ItemStack): NbtCompound = stack.nbt!!.getCompound("charmed")
	fun getHex(stack: ItemStack, world: ServerWorld) = (IotaType.deserialize(getCompound(stack).getCompound("instructions"), world) as ListIota).list.toList()
	fun getMedia(stack: ItemStack) = getCompound(stack).getLong("media")
	fun getMaxMedia(stack: ItemStack) = getCompound(stack).getLong("max_media")
}