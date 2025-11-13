package miyucomics.hexical.features.specklikes.speck

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import miyucomics.hexical.misc.SimpleEntityIotaHolder
import miyucomics.hexpose.iotas.TextIota
import net.minecraft.nbt.NbtCompound

class SpeckChronicler(val speck: SpeckEntity) : SimpleEntityIotaHolder() {
	override fun writeable() = true
	override fun readIotaTag(): NbtCompound = IotaType.serialize(TextIota(speck.dataTracker.get(SpeckEntity.textDataTracker)))
	override fun writeIota(iota: Iota?, simulate: Boolean): Boolean {
		if (iota == null)
			return false
		speck.setText(iota.display())
		return true
	}
}