package miyucomics.hexical.data.hopper.targets

import miyucomics.hexical.data.hopper.HopperDestination
import miyucomics.hexical.data.hopper.HopperSource
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class InventoryEndpoint(private val inventory: Inventory) : HopperSource, HopperDestination {
	override fun getItems(): List<ItemStack> {
		return (0 until inventory.size()).map { inventory.getStack(it).copy() }.filterNot { it.isEmpty }
	}

	override fun withdraw(stack: ItemStack, amount: Int): Boolean {
		var remaining = amount

		for (i in 0 until inventory.size()) {
			val existing = inventory.getStack(i)
			if (!ItemStack.areItemsEqual(existing, stack)) continue
			if (existing.isEmpty) continue

			val toTake = remaining.coerceAtMost(existing.count)
			existing.decrement(toTake)
			remaining -= toTake

			if (remaining <= 0) return true
		}

		// Failed to withdraw full amount — no rollback, so this leaves inventory partially changed
		return false
	}

	override fun deposit(stack: ItemStack): ItemStack {
		val remaining = stack.copy()

		// First, try to merge into existing stacks
		for (i in 0 until inventory.size()) {
			if (!inventory.isValid(i, remaining)) continue
			val existing = inventory.getStack(i)
			if (!ItemStack.canCombine(existing, remaining)) continue

			val canAdd = existing.maxCount - existing.count
			val toAdd = remaining.count.coerceAtMost(canAdd)
			if (toAdd > 0) {
				existing.increment(toAdd)
				remaining.decrement(toAdd)
				if (remaining.isEmpty) return ItemStack.EMPTY
			}
		}

		// Then, try to place in empty slots
		for (i in 0 until inventory.size()) {
			if (!inventory.isValid(i, remaining)) continue
			val existing = inventory.getStack(i)
			if (!existing.isEmpty) continue

			val toPlace = remaining.copy()
			val placedCount = toPlace.count.coerceAtMost(toPlace.maxCount)
			toPlace.count = placedCount
			inventory.setStack(i, toPlace)
			remaining.decrement(placedCount)

			if (remaining.isEmpty) return ItemStack.EMPTY
		}

		return remaining
	}

	override fun simulateDeposit(stack: ItemStack): Int {
		var remaining = stack.count
		val maxStackSize = stack.maxCount

		// Simulate merging into existing stacks
		for (i in 0 until inventory.size()) {
			val existing = inventory.getStack(i)
			if (!inventory.isValid(i, stack)) continue
			if (ItemStack.canCombine(existing, stack)) {
				val space = existing.maxCount - existing.count
				val toInsert = remaining.coerceAtMost(space)
				remaining -= toInsert
				if (remaining <= 0) return stack.count
			}
		}

		// Simulate inserting into empty slots
		for (i in 0 until inventory.size()) {
			val existing = inventory.getStack(i)
			if (!inventory.isValid(i, stack)) continue
			if (!existing.isEmpty) continue

			val toInsert = remaining.coerceAtMost(maxStackSize)
			remaining -= toInsert
			if (remaining <= 0) return stack.count
		}

		return stack.count - remaining
	}
}