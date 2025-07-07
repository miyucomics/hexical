package miyucomics.hexical.data.hopper.targets

import miyucomics.hexical.data.hopper.HopperEndpoint
import net.minecraft.command.argument.EntityArgumentType.player
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

class ItemFrameEndpoint(val frame: ItemFrameEntity) : HopperEndpoint.Source {
	override fun getItems(): List<ItemStack> {
		val stack = frame.heldItemStack
		return if (stack.isEmpty) emptyList() else listOf(stack.copy())
	}

	override fun remove(stack: ItemStack, amount: Int): Boolean {
		val existing = frame.heldItemStack
		if (!ItemStack.areItemsEqual(existing, stack)) return false
		if (existing.count < amount) return false

		existing.decrement(amount)

		if (existing.isEmpty) {
			frame.setHeldItemStack(ItemStack.EMPTY, true)
		} else {
			frame.setHeldItemStack(existing, true)
		}

		return true
	}

	override fun insert(stack: ItemStack): ItemStack {
		val existing = frame.heldItemStack
		if (existing.isEmpty) {
			val placed = stack.copy()
			placed.count = 1
			frame.setHeldItemStack(placed, true)
			return if (stack.count > 1) stack.copy().also { it.decrement(1) } else ItemStack.EMPTY
		}

		return stack
	}
}