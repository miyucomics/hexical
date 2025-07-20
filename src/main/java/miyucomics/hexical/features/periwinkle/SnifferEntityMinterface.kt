package miyucomics.hexical.features.periwinkle

import net.minecraft.item.ItemStack

interface SnifferEntityMinterface {
	fun produceItem(stack: ItemStack)
	fun isDiggingCustom(): Boolean
}