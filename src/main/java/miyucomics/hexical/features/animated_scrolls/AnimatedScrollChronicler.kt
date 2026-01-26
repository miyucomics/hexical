package miyucomics.hexical.features.animated_scrolls

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.PatternIota
import miyucomics.hexical.misc.SimpleEntityIotaHolder
import net.minecraft.nbt.NbtCompound

class AnimatedScrollChronicler(val scroll: AnimatedScrollEntity) : SimpleEntityIotaHolder() {
	override fun writeable() = true
	override fun readIotaTag(): NbtCompound? = scroll.pattern?.let { IotaType.serialize(PatternIota(it)) }
	override fun writeIota(iota: Iota?, simulate: Boolean): Boolean = when (iota) {
		null -> {
			scroll.setPatternAndUpdate(null)
			true
		}
		is PatternIota -> {
			scroll.setPatternAndUpdate(iota.pattern)
			true
		}
		else -> false
	}
}