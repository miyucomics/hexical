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

object TweakedItemsUtils {
	@JvmField
	val PERCENTAGE: DecimalFormat = DecimalFormat("####")
	@JvmField
	val DUST_AMOUNT: DecimalFormat = DecimalFormat("###,###.##")

	init {
		PERCENTAGE.roundingMode = RoundingMode.DOWN;
	}

	@JvmStatic
	fun getTweakedItem(player: PlayerEntity): Pair<Hand, ItemStack>? {
		if (isStackTweaked(player.getStackInHand(Hand.OFF_HAND))) return Pair(Hand.OFF_HAND, player.getStackInHand(Hand.OFF_HAND))
		if (isStackTweaked(player.getStackInHand(Hand.MAIN_HAND))) return Pair(Hand.MAIN_HAND, player.getStackInHand(Hand.MAIN_HAND))
		return null
	}

	@JvmStatic
	fun isStackTweaked(stack: ItemStack): Boolean {
		return stack.hasNbt() && stack.nbt!!.contains("hex_tweak")
	}

	@JvmStatic
	fun getBoolean(stack: ItemStack, target: String): Boolean {
		return stack.nbt!!.getCompound("hex_tweak").getBoolean(target)
	}

	@JvmStatic
	fun getHex(stack: ItemStack, world: ServerWorld): List<Iota> {
		return (IotaType.deserialize(stack.nbt!!.getCompound("hex_tweak").getCompound("instructions"), world) as ListIota).list.toList()
	}

	@JvmStatic
	fun getMedia(stack: ItemStack): Long {
		return stack.nbt!!.getCompound("hex_tweak").getLong("media")
	}

	@JvmStatic
	fun getMaxMedia(stack: ItemStack): Long {
		return stack.nbt!!.getCompound("hex_tweak").getLong("max_media")
	}

	@JvmStatic
	fun deductMedia(stack: ItemStack, cost: Long) {
		val oldMedia = getMedia(stack)
		if (oldMedia == cost)
			stack.nbt!!.remove("hex_tweak")
		else
			stack.nbt!!.getCompound("hex_tweak").putLong("media", oldMedia - cost)
	}
}