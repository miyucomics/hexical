package miyucomics.hexical.features.specklikes.strand

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.misc.SimpleEntityIotaHolder
import net.minecraft.nbt.NbtCompound

class StrandChronicler(val strand: StrandEntity) : SimpleEntityIotaHolder() {
	override fun writeable() = true
	override fun writeIota(iota: Iota?, simulate: Boolean) = false
	override fun readIotaTag(): NbtCompound = IotaType.serialize(NullIota())
}