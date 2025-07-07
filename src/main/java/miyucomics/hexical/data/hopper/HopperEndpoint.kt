package miyucomics.hexical.data.hopper

import net.minecraft.item.ItemStack

interface HopperEndpoint {
	fun insert(stack: ItemStack): ItemStack

	interface Source : HopperEndpoint {
		fun getItems(): List<ItemStack>
		fun remove(stack: ItemStack, amount: Int): Boolean
	}
}