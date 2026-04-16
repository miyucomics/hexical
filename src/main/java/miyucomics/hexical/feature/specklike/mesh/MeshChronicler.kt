package miyucomics.hexical.feature.specklike.mesh

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.misc.SimpleEntityIotaHolder
import net.minecraft.nbt.NbtCompound
import org.joml.Vector3f

class MeshChronicler(val mesh: MeshEntity) : SimpleEntityIotaHolder() {
	override fun writeable() = true
	override fun readIotaTag(): NbtCompound = IotaType.serialize(ListIota(mesh.getShape()))
	override fun writeIota(iota: Iota?, simulate: Boolean): Boolean {
		if (iota !is ListIota)
			return false
		if (iota.size() > 32)
			return false

		val points = iota.list.map {
			if (it !is Vec3Iota)
				return false
			val vector = it.vec3
			if (vector.lengthSquared() > 100)
				return false
			Vector3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat())
		}

		if (!simulate)
			mesh.setShape(points)

		return true
	}
}