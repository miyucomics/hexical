package miyucomics.hexical.interfaces

import net.minecraft.item.ItemStack

interface PlayerEntityMinterface {
	fun getArchLampCastedThisTick(): Boolean
	fun lampCastedThisTick()
	fun stashWristpocket(stack: ItemStack)
	fun wristpocketItem(): ItemStack
}