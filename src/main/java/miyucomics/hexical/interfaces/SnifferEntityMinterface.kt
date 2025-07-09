package miyucomics.hexical.interfaces

import net.minecraft.item.ItemStack

interface SnifferEntityMinterface {
	fun produceItem(stack: ItemStack)
	fun hasCustomItem(): Boolean
}