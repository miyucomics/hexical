package miyucomics.hexical.features.specklikes.strand

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import miyucomics.hexical.misc.SimpleEntityIotaHolder
import miyucomics.hexpose.iotas.TextIota
import net.minecraft.nbt.NbtCompound

class StrandChronicler(val strand: StrandEntity) : SimpleEntityIotaHolder() {
	override fun writeable() = true
	override fun readIotaTag(): NbtCompound = IotaType.serialize(TextIota(strand.dataTracker.get(StrandEntity.textDataTracker)))
	override fun writeIota(iota: Iota?, simulate: Boolean): Boolean {
		if (iota == null)
			return false
		strand.setText(iota.display())
		return true
	}
}