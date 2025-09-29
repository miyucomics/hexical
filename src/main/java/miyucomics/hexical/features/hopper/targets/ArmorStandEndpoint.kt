package miyucomics.hexical.features.hopper.targets

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

class ArmorStandEndpoint(private val armorItems: DefaultedList<ItemStack>, private val heldItems: DefaultedList<ItemStack>) : Inventory {
	override fun getStack(slot: Int): ItemStack {
		return when (slot) {
			in 0..3 -> armorItems[slot]
			in 4..5 -> heldItems[slot - 4]
			else -> throw IndexOutOfBoundsException("$slot out of bounds for ArmorStandEndpoint")
		}
	}

	override fun removeStack(slot: Int, amount: Int): ItemStack {
		val stack = getStack(slot)
		return if (stack.isEmpty) ItemStack.EMPTY else stack.split(amount)
	}

	override fun removeStack(slot: Int): ItemStack {
		val result = getStack(slot)
		setStack(slot, ItemStack.EMPTY)
		return result
	}

	override fun setStack(slot: Int, stack: ItemStack) {
		when (slot) {
			in 0..3 -> armorItems[slot] = stack
			in 4..5 -> heldItems[slot - 4] = stack
			else -> throw IndexOutOfBoundsException("$slot out of bounds for ArmorStandInventory")
		}
	}

	override fun clear() {
		for (i in 0 until 4) armorItems[i] = ItemStack.EMPTY
		for (i in 0 until 2) heldItems[i] = ItemStack.EMPTY
	}

	override fun size() = 6
	override fun markDirty() {}
	override fun canPlayerUse(player: PlayerEntity?): Boolean = true
	override fun isEmpty() = armorItems.all { it.isEmpty } && heldItems.all { it.isEmpty }
}