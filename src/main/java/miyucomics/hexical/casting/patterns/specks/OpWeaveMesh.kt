package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.entities.MeshEntity
import net.minecraft.util.math.Vec3f

class OpWeaveMesh : ConstMediaAction {
	override val argc = 2
	override val mediaCost: Int = MediaConstants.DUST_UNIT / 100
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val mesh = args.getEntity(0, argc)
		if (mesh !is MeshEntity)
			throw MishapBadEntity.of(mesh, "mesh")

		val design = args.getList(1, argc)
		if (design.size() > 32)
			throw MishapInvalidIota.of(args[0], 0, "mesh_design")
		val points = mutableListOf<Vec3f>()
		for (point in design) {
			if (point.type != Vec3Iota.TYPE)
				throw MishapInvalidIota.of(args[0], 0, "mesh_design")
			else {
				val vector = (point as Vec3Iota).vec3
				if (vector.lengthSquared() > 10 * 10)
					throw MishapInvalidIota.of(args[0], 0, "mesh_design")
				points.add(Vec3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat()))
			}
		}
		mesh.setShape(points)
		return listOf()
	}
}