package miyucomics.hexical.feature.periwinkle

import net.minecraft.item.ItemStack

interface SnifferEntityMinterface {
	fun produceItem(stack: ItemStack)
	fun isDiggingCustom(): Boolean
}