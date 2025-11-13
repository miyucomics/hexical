package miyucomics.hexical.misc

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.fabric.cc.adimpl.CCEntityIotaHolder
import net.minecraft.nbt.NbtCompound

abstract class SimpleEntityIotaHolder : CCEntityIotaHolder() {
	abstract override fun readIotaTag(): NbtCompound
	abstract override fun writeIota(iota: Iota?, simulate: Boolean): Boolean
	abstract override fun writeable(): Boolean
}