package miyucomics.hexical.features.specklikes.mesh

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kosmx.playerAnim.core.util.Vec3f

object OpWeaveMesh : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val mesh = args.getEntity(0, argc)
		if (mesh !is MeshEntity)
			throw MishapBadEntity.of(mesh, "mesh")

		val design = args.getList(1, argc)
		if (design.size() > 32)
			throw MishapInvalidIota.of(args[1], 0, "mesh_design")
		val points = design.map {
			if (it !is Vec3Iota)
				throw MishapInvalidIota.of(args[1], 0, "mesh_design")
			val vector = it.vec3
			if (vector.length() > 10)
				throw MishapInvalidIota.of(args[1], 0, "mesh_design")
			Vec3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat())
		}

		mesh.setShape(points)
		return listOf()
	}
}