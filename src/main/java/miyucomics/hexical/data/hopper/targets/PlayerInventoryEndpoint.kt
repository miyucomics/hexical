package miyucomics.hexical.data.hopper.targets

import miyucomics.hexical.data.hopper.HopperEndpoint
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

class PlayerInventoryEndpoint(val player: ServerPlayerEntity) : HopperEndpoint.Source {
	override fun getItems(): List<ItemStack> = player.inventory.main.toList()

	override fun insert(stack: ItemStack): ItemStack {
		val leftover = player.inventory.insertStack(stack)
//		return leftover
		return ItemStack.EMPTY
	}

	override fun remove(stack: ItemStack, amount: Int): Boolean {
		val index = player.inventory.main.indexOfFirst { ItemStack.areItemsEqual(it, stack) }
		if (index == -1)
			return false
		val existing = player.inventory.main[index]
		if (existing.count < amount)
			return false
		existing.decrement(amount)
		return true
	}
}