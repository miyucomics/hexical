package miyucomics.hexical.features.specklikes.mesh

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.misc.SimpleEntityIotaHolder
import net.minecraft.nbt.NbtCompound

class MeshChronicler(val mesh: MeshEntity) : SimpleEntityIotaHolder() {
	override fun writeable() = true
	override fun readIotaTag(): NbtCompound = IotaType.serialize(ListIota(mesh.getShape()))
	override fun writeIota(design: Iota?, simulate: Boolean): Boolean {
		if (design !is ListIota)
			return false
		if (design.size() > 32)
			return false

		val points = design.list.map {
			if (it !is Vec3Iota)
				return false
			val vector = it.vec3
			if (vector.length() > 10)
				return false
			Vec3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat())
		}
		mesh.setShape(points)

		return true
	}
}