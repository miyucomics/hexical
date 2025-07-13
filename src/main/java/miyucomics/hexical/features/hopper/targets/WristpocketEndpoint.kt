package miyucomics.hexical.features.hopper.targets

import miyucomics.hexical.features.hopper.HopperDestination
import miyucomics.hexical.features.hopper.HopperSource
import miyucomics.hexical.features.player_state.fields.wristpocket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class WristpocketEndpoint(private val player: PlayerEntity) : HopperSource, HopperDestination {
	override fun getItems(): List<ItemStack> {
		val stack = player.wristpocket
		return if (stack.isEmpty) emptyList() else listOf(stack.copy())
	}

	override fun withdraw(stack: ItemStack, amount: Int): Boolean {
		val existing = player.wristpocket
		if (!ItemStack.areItemsEqual(existing, stack)) return false
		if (existing.count < amount) return false

		existing.decrement(amount)
		player.wristpocket = if (existing.isEmpty) ItemStack.EMPTY else existing
		return true
	}

	override fun deposit(stack: ItemStack): ItemStack {
		val current = player.wristpocket

		if (current.isEmpty) {
			val toInsert = stack.copy()
			val insertCount = toInsert.count.coerceAtMost(toInsert.maxCount)
			toInsert.count = insertCount
			player.wristpocket = toInsert
			return stack.copy().apply { decrement(insertCount) }.takeIf { !it.isEmpty } ?: ItemStack.EMPTY
		}

		if (ItemStack.canCombine(current, stack)) {
			val space = current.maxCount - current.count
			if (space > 0) {
				val toAdd = stack.count.coerceAtMost(space)
				current.increment(toAdd)
				stack.decrement(toAdd)
				player.wristpocket = current
			}
		}

		return stack
	}

	override fun simulateDeposit(stack: ItemStack): Int {
		val current = player.wristpocket
		return when {
			current.isEmpty -> stack.count.coerceAtMost(stack.maxCount)
			ItemStack.canCombine(current, stack) -> (current.maxCount - current.count).coerceAtLeast(0).coerceAtMost(stack.count)
			else -> 0
		}
	}
}