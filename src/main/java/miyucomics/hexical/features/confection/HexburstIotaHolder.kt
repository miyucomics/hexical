package miyucomics.hexical.features.confection

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.fabric.cc.adimpl.CCItemIotaHolder
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class HexburstIotaHolder(val hexburst: ItemStack) : CCItemIotaHolder(hexburst) {
	override fun writeable() = false
	override fun writeIota(iota: Iota?, simulate: Boolean) = false
	override fun readIotaTag(): NbtCompound {
		hexburst.increment(-1)
		return hexburst.orCreateNbt.getCompound("iota")
	}
}