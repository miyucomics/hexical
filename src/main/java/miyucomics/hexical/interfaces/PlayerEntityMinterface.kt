package miyucomics.hexical.interfaces

import miyucomics.hexical.data.ArchLampState
import miyucomics.hexical.data.LesserSentinelState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

interface PlayerEntityMinterface {
	fun getArchLampCastedThisTick(): Boolean
	fun getArchLampState(): ArchLampState
	fun archLampCasted()

	fun getWristpocket(): ItemStack
	fun setWristpocket(stack: ItemStack)

	fun getEvocation(): NbtCompound
	fun setEvocation(hex: NbtCompound)

	fun getLesserSentinels(): LesserSentinelState
	fun setLesserSentinels(state: LesserSentinelState)
}