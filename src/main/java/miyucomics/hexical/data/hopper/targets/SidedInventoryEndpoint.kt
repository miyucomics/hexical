package miyucomics.hexical.data.hopper.targets

import miyucomics.hexical.data.hopper.HopperDestination
import miyucomics.hexical.data.hopper.HopperSource
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class SidedInventoryEndpoint(private val inventory: SidedInventory, private val direction: Direction) : HopperSource, HopperDestination {
	override fun getItems(): List<ItemStack> {
		val slots = inventory.getAvailableSlots(direction)
		return slots.map { inventory.getStack(it).copy() }.filterNot { it.isEmpty }
	}

	override fun withdraw(stack: ItemStack, amount: Int): Boolean {
		var remaining = amount
		val slots = inventory.getAvailableSlots(direction)

		for (slot in slots) {
			val existing = inventory.getStack(slot)

			if (!ItemStack.areItemsEqual(existing, stack)) continue
			if (!inventory.canExtract(slot, stack, direction)) continue

			val toTake = remaining.coerceAtMost(existing.count)
			existing.decrement(toTake)
			remaining -= toTake

			if (remaining <= 0) return true
		}

		return false
	}

	override fun simulateDeposit(stack: ItemStack): Int {
		var remaining = stack.count
		val slots = inventory.getAvailableSlots(direction)

		for (slot in slots) {
			if (!inventory.canInsert(slot, stack, direction)) continue

			val existing = inventory.getStack(slot)

			if (existing.isEmpty) {
				val toInsert = remaining.coerceAtMost(stack.maxCount)
				remaining -= toInsert
			} else if (ItemStack.canCombine(existing, stack)) {
				val space = existing.maxCount - existing.count
				val toInsert = remaining.coerceAtMost(space)
				remaining -= toInsert
			}

			if (remaining <= 0) break
		}

		return stack.count - remaining
	}

	override fun deposit(stack: ItemStack): ItemStack {
		val working = stack.copy()
		val slots = inventory.getAvailableSlots(direction)

		for (slot in slots) {
			if (!inventory.canInsert(slot, working, direction)) continue

			val existing = inventory.getStack(slot)

			if (existing.isEmpty) {
				val placed = working.copy()
				val toPlace = working.count.coerceAtMost(placed.maxCount)
				placed.count = toPlace
				inventory.setStack(slot, placed)
				working.decrement(toPlace)
			} else if (ItemStack.canCombine(existing, working)) {
				val space = existing.maxCount - existing.count
				val toAdd = working.count.coerceAtMost(space)
				existing.increment(toAdd)
				working.decrement(toAdd)
			}

			if (working.isEmpty) break
		}

		return if (working.isEmpty) ItemStack.EMPTY else working
	}
}