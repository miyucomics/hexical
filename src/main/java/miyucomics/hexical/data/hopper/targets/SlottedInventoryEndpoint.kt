package miyucomics.hexical.data.hopper.targets

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.casting.mishaps.NotEnoughSlotsMishap
import miyucomics.hexical.data.hopper.HopperDestination
import miyucomics.hexical.data.hopper.HopperSource
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class SlottedInventoryEndpoint(private val inventory: Inventory, private val slot: Int, iota: Iota) : HopperSource, HopperDestination {
	init {
		if (slot >= inventory.size() || slot < 0)
			throw NotEnoughSlotsMishap(iota, slot)
	}

	override fun getItems(): List<ItemStack> {
		val stack = inventory.getStack(slot).copy()
		return if (stack.isEmpty) emptyList() else listOf(stack)
	}

	override fun withdraw(stack: ItemStack, amount: Int): Boolean {
		val existing = inventory.getStack(slot)
		if (!ItemStack.areItemsEqual(existing, stack)) return false
		if (existing.count < amount) return false

		existing.decrement(amount)
		return true
	}

	override fun deposit(stack: ItemStack): ItemStack {
		val target = inventory.getStack(slot)

		if (!inventory.isValid(slot, stack)) return stack.copy()

		if (target.isEmpty) {
			// Place only if exactly 1 stack can be deposited (like vanilla hopper logic)
			val toPlace = stack.copy()
			val amount = toPlace.count.coerceAtMost(stack.maxCount)
			toPlace.count = amount
			inventory.setStack(slot, toPlace)
			return if (stack.count > amount) stack.copy().apply { decrement(amount) } else ItemStack.EMPTY
		}

		if (!ItemStack.canCombine(target, stack)) return stack.copy()

		val canAdd = target.maxCount - target.count
		if (canAdd <= 0) return stack.copy()

		val toAdd = stack.count.coerceAtMost(canAdd)
		target.increment(toAdd)

		return if (stack.count > toAdd) stack.copy().apply { decrement(toAdd) } else ItemStack.EMPTY
	}

	override fun simulateDeposit(stack: ItemStack): Int {
		val target = inventory.getStack(slot)

		if (!inventory.isValid(slot, stack)) return 0

		if (target.isEmpty) {
			return stack.count.coerceAtMost(stack.maxCount)
		}

		if (!ItemStack.canCombine(target, stack)) return 0

		val space = target.maxCount - target.count
		return stack.count.coerceAtMost(space)
	}
}