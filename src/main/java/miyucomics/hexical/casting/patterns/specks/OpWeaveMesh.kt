package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.entities.specklikes.MeshEntity

class OpWeaveMesh : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val mesh = args.getEntity(0, argc)
		if (mesh !is MeshEntity)
			throw MishapBadEntity.of(mesh, "mesh")

		val design = args.getList(1, argc)
		if (design.size() > 32)
			throw MishapInvalidIota.of(args[1], 1, "mesh_design")
		val points = mutableListOf<Vec3f>()
		for (point in design) {
			if (point.type != Vec3Iota.TYPE)
				throw MishapInvalidIota.of(args[1], 1, "mesh_design")
			else {
				val vector = (point as Vec3Iota).vec3
				if (vector.length() > 10)
					throw MishapInvalidIota.of(args[1], 1, "mesh_design")
				points.add(Vec3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat()))
			}
		}
		mesh.setShape(points)
		return listOf()
	}
}