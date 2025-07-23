package miyucomics.hexical.features.hopper.targets

import miyucomics.hexical.features.hopper.HopperDestination
import miyucomics.hexical.features.hopper.HopperSource
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class InventoryEndpoint(val inventory: Inventory) : HopperSource, HopperDestination {
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
			remaining -= inventory.removeStack(i, toTake).count
			if (remaining <= 0) return true
		}

		return false
	}

	override fun deposit(stack: ItemStack): ItemStack {
		val remaining = stack.copy()

		// First, try to merge into existing stacks
		for (slot in 0 until inventory.size()) {
			if (!inventory.isValid(slot, remaining)) continue
			val existing = inventory.getStack(slot)
			if (!ItemStack.canCombine(existing, remaining)) continue

			val slotLimit = minOf(inventory.maxCountPerStack, remaining.maxCount)
			val canAdd = slotLimit - existing.count
			val toAdd = remaining.count.coerceAtMost(canAdd)
			if (toAdd > 0) {
				existing.increment(toAdd)
				remaining.decrement(toAdd)
				if (remaining.isEmpty) return ItemStack.EMPTY
			}
		}

		for (i in 0 until inventory.size()) {
			if (!inventory.isValid(i, remaining)) continue
			val existing = inventory.getStack(i)
			if (!existing.isEmpty) continue

			val slotLimit = minOf(inventory.maxCountPerStack, remaining.maxCount)
			val toPlace = remaining.copy()
			val placedCount = toPlace.count.coerceAtMost(slotLimit)
			toPlace.count = placedCount
			inventory.setStack(i, toPlace)
			remaining.decrement(placedCount)

			if (remaining.isEmpty) return ItemStack.EMPTY
		}

		return remaining
	}

	override fun simulateDeposit(stack: ItemStack): Int {
		var remaining = stack.count

		// First, try to merge into existing stacks
		for (i in 0 until inventory.size()) {
			val existing = inventory.getStack(i)
			if (!inventory.isValid(i, stack)) continue
			if (ItemStack.canCombine(existing, stack)) {
				val slotLimit = minOf(inventory.maxCountPerStack, existing.maxCount)
				val space = slotLimit - existing.count
				val toInsert = remaining.coerceAtMost(space)
				remaining -= toInsert
				if (remaining <= 0) return stack.count
			}
		}

		val effectiveMax = minOf(stack.maxCount, inventory.maxCountPerStack)
		for (i in 0 until inventory.size()) {
			val existing = inventory.getStack(i)
			if (!inventory.isValid(i, stack)) continue
			if (!existing.isEmpty) continue

			val toInsert = remaining.coerceAtMost(effectiveMax)
			remaining -= toInsert
			if (remaining <= 0) return stack.count
		}

		return stack.count - remaining
	}
}