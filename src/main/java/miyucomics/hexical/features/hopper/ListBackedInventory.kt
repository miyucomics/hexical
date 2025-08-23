package miyucomics.hexical.features.hopper

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class ListBackedInventory(private val stacks: MutableList<ItemStack>, val size: Int = stacks.size) : Inventory {
	override fun removeStack(slot: Int, amount: Int): ItemStack = stacks[slot].split(amount)

	override fun removeStack(slot: Int): ItemStack {
		val original = stacks[slot]
		stacks[slot] = ItemStack.EMPTY
		return original
	}

	override fun setStack(slot: Int, stack: ItemStack) {
		stacks[slot] = stack
	}

	override fun clear() {
		for (i in stacks.indices)
			stacks[i] = ItemStack.EMPTY
	}

	override fun size() = size
	override fun markDirty() {}
	override fun getStack(slot: Int) = stacks[slot]
	override fun isEmpty() = stacks.all { it.isEmpty }
	override fun canPlayerUse(player: PlayerEntity?) = true
}
