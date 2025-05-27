package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import java.math.RoundingMode
import java.text.DecimalFormat

object CharmedItemUtilities {
	@JvmField
	val PERCENTAGE: DecimalFormat = DecimalFormat("####")
	@JvmField
	val DUST_AMOUNT: DecimalFormat = DecimalFormat("###,###.##")

	init {
		PERCENTAGE.roundingMode = RoundingMode.DOWN;
	}

	@JvmStatic
	fun getCharmedItem(player: PlayerEntity): Pair<Hand, ItemStack>? {
		if (isStackCharmed(player.getStackInHand(Hand.OFF_HAND))) return Pair(Hand.OFF_HAND, player.getStackInHand(Hand.OFF_HAND))
		if (isStackCharmed(player.getStackInHand(Hand.MAIN_HAND))) return Pair(Hand.MAIN_HAND, player.getStackInHand(Hand.MAIN_HAND))
		return null
	}

	@JvmStatic
	fun isStackCharmed(stack: ItemStack): Boolean {
		return stack.hasNbt() && stack.nbt!!.contains("charmed")
	}

	@JvmStatic
	fun removeCharm(stack: ItemStack) {
		stack.nbt!!.remove("charmed")
	}

	@JvmStatic
	fun getBoolean(stack: ItemStack, target: String): Boolean {
		return stack.nbt!!.getCompound("charmed").getBoolean(target)
	}

	@JvmStatic
	fun getHex(stack: ItemStack, world: ServerWorld): List<Iota> {
		return (IotaType.deserialize(stack.nbt!!.getCompound("charmed").getCompound("instructions"), world) as ListIota).list.toList()
	}

	@JvmStatic
	fun getMedia(stack: ItemStack): Long {
		return stack.nbt!!.getCompound("charmed").getLong("media")
	}

	@JvmStatic
	fun getMaxMedia(stack: ItemStack): Long {
		return stack.nbt!!.getCompound("charmed").getLong("max_media")
	}

	@JvmStatic
	fun deductMedia(stack: ItemStack, cost: Long) {
		val oldMedia = getMedia(stack)
		if (oldMedia <= cost)
			removeCharm(stack)
		else
			stack.nbt!!.getCompound("charmed").putLong("media", oldMedia - cost)
	}
}