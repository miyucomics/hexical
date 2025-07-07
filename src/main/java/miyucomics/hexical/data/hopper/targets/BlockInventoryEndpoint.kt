package miyucomics.hexical.data.hopper.targets

import miyucomics.hexical.data.hopper.HopperEndpoint
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class BlockInventoryEndpoint(val handler: Inventory) : HopperEndpoint.Source {
	override fun getItems(): List<ItemStack> = (0 until handler.size()).map { handler.getStack(it) }

	override fun insert(stack: ItemStack): ItemStack {
		for (i in 0 until handler.size()) {
			if (handler.isValid(i, stack)) {
				val existing = handler.getStack(i)
				if (existing.isEmpty) {
					handler.setStack(i, stack.copy())
					return ItemStack.EMPTY
				} else if (ItemStack.canCombine(existing, stack)) {
					val canAdd = existing.maxCount - existing.count
					val toAdd = stack.count.coerceAtMost(canAdd)
					existing.increment(toAdd)
					stack.decrement(toAdd)
					if (stack.isEmpty) return ItemStack.EMPTY
				}
			}
		}
		return stack
	}

	override fun remove(stack: ItemStack, amount: Int): Boolean {
		for (i in 0 until handler.size()) {
			val existing = handler.getStack(i)
			if (ItemStack.areItemsEqual(existing, stack) && existing.count >= amount) {
				existing.decrement(amount)
				return true
			}
		}
		return false
	}
}