package miyucomics.hexical.data.hopper

import net.minecraft.item.ItemStack

interface HopperEndpoint

interface HopperSource : HopperEndpoint {
	fun getItems(): List<ItemStack>
	fun withdraw(stack: ItemStack, amount: Int): Boolean
}

interface HopperDestination : HopperEndpoint {
	/**
	* Simulates how many items from the stack could be accepted.
	* Must not mutate anything.
	*/
	fun simulateDeposit(stack: ItemStack): Int

	/**
	* Actually deposits items. May mutate world state.
	* Returns leftover stack (i.e., what couldn't be inserted).
	*/
	fun deposit(stack: ItemStack): ItemStack
}