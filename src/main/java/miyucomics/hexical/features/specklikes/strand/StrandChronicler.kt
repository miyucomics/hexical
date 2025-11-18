package miyucomics.hexical.features.specklikes.strand

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.misc.SimpleEntityIotaHolder
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec2f
import kotlin.math.pow

class StrandChronicler(val strand: StrandEntity) : SimpleEntityIotaHolder() {
	override fun writeable() = true
	override fun readIotaTag(): NbtCompound = IotaType.serialize(ListIota(strand.getShape()))
	override fun writeIota(design: Iota?, simulate: Boolean): Boolean {
		if (design !is ListIota)
			return false
		if (design.size() > 32)
			return false

		val points = design.list.map {
			if (it !is Vec3Iota)
				return false
			val vector = it.vec3
			if (vector.x.pow(2) + vector.y.pow(2) > 100)
				return false
			Vec2f(vector.x.toFloat(), vector.y.toFloat())
		}

		if (!simulate)
			strand.setShape(points)

		return true
	}
}