package miyucomics.hexical.features.lei

import net.minecraft.item.ItemStack

interface SnifferEntityMinterface {
	fun produceItem(stack: ItemStack)
	fun isDiggingCustom(): Boolean
}