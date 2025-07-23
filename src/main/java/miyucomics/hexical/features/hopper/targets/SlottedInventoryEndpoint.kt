package miyucomics.hexical.features.hopper.targets

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.features.hopper.HopperDestination
import miyucomics.hexical.features.hopper.HopperSource
import miyucomics.hexical.features.hopper.InvalidSlotMishap
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class SlottedInventoryEndpoint(private val inventory: Inventory, private val slot: Int, iota: Iota) : HopperSource, HopperDestination {
	init {
		if (slot !in 0 until inventory.size())
			throw InvalidSlotMishap(iota, slot)
	}

	override fun getItems(): List<ItemStack> {
		val stack = inventory.getStack(slot)
		return if (stack.isEmpty) emptyList() else listOf(stack.copy())
	}

	override fun withdraw(stack: ItemStack, amount: Int): Boolean {
		val existing = inventory.getStack(slot)
		if (!ItemStack.areItemsEqual(existing, stack)) return false
		if (existing.count < amount) return false
		if (!inventory.isValid(slot, stack)) return false

		existing.decrement(amount)
		return true
	}

	override fun deposit(stack: ItemStack): ItemStack {
		if (!inventory.isValid(slot, stack)) return stack.copy()
		val slotLimit = minOf(stack.maxCount, inventory.maxCountPerStack)

		val existingStack = inventory.getStack(slot)
		if (existingStack.isEmpty) {
			val insertedStack = stack.copy()
			val amount = insertedStack.count.coerceAtMost(slotLimit)
			insertedStack.count = amount
			inventory.setStack(slot, insertedStack)
			return if (stack.count > amount) stack.copy().apply { decrement(amount) } else ItemStack.EMPTY
		}

		if (!ItemStack.canCombine(existingStack, stack)) return stack.copy()
		val amountToInsert = slotLimit - existingStack.count
		if (amountToInsert <= 0) return stack.copy()
		val insertedStack = stack.count.coerceAtMost(amountToInsert)
		existingStack.increment(insertedStack)
		return if (stack.count > insertedStack) stack.copy().apply { decrement(insertedStack) } else ItemStack.EMPTY
	}

	override fun simulateDeposit(stack: ItemStack): Int {
		val target = inventory.getStack(slot)
		if (!inventory.isValid(slot, stack)) return 0
		val slotLimit = minOf(stack.maxCount, inventory.maxCountPerStack)
		if (target.isEmpty) return stack.count.coerceAtMost(slotLimit)
		if (!ItemStack.canCombine(target, stack)) return 0
		val space = slotLimit - target.count
		return stack.count.coerceAtMost(space)
	}
}