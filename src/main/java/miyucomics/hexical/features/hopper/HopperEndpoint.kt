package miyucomics.hexical.features.hopper

import net.minecraft.item.ItemStack

interface HopperEndpoint

interface HopperSource : HopperEndpoint {
	fun getItems(): List<ItemStack>
	fun withdraw(stack: ItemStack, amount: Int): Boolean
}

interface HopperDestination : HopperEndpoint {
	fun simulateDeposits(stacks: List<ItemStack>): Map<ItemStack, Int>
	fun deposit(stack: ItemStack): ItemStack
}