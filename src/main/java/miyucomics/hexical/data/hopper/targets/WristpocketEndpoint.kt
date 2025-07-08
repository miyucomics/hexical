package miyucomics.hexical.data.hopper.targets

import miyucomics.hexical.data.hopper.HopperDestination
import miyucomics.hexical.data.hopper.HopperSource
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.item.ItemStack

class WristpocketEndpoint(private val player: PlayerEntityMinterface) : HopperSource, HopperDestination {
	override fun getItems(): List<ItemStack> {
		val stack = player.getWristpocket()
		return if (stack.isEmpty) emptyList() else listOf(stack.copy())
	}

	override fun withdraw(stack: ItemStack, amount: Int): Boolean {
		val existing = player.getWristpocket()
		if (!ItemStack.areItemsEqual(existing, stack)) return false
		if (existing.count < amount) return false

		existing.decrement(amount)
		player.setWristpocket(if (existing.isEmpty) ItemStack.EMPTY else existing)
		return true
	}

	override fun deposit(stack: ItemStack): ItemStack {
		val current = player.getWristpocket()

		if (current.isEmpty) {
			val toInsert = stack.copy()
			val insertCount = toInsert.count.coerceAtMost(toInsert.maxCount)
			toInsert.count = insertCount
			player.setWristpocket(toInsert)
			return stack.copy().apply { decrement(insertCount) }.takeIf { !it.isEmpty } ?: ItemStack.EMPTY
		}

		if (ItemStack.canCombine(current, stack)) {
			val space = current.maxCount - current.count
			if (space > 0) {
				val toAdd = stack.count.coerceAtMost(space)
				current.increment(toAdd)
				stack.decrement(toAdd)
				player.setWristpocket(current)
			}
		}

		return stack
	}

	override fun simulateDeposit(stack: ItemStack): Int {
		val current = player.getWristpocket()
		return when {
			current.isEmpty -> stack.count.coerceAtMost(stack.maxCount)
			ItemStack.canCombine(current, stack) -> (current.maxCount - current.count).coerceAtLeast(0).coerceAtMost(stack.count)
			else -> 0
		}
	}
}