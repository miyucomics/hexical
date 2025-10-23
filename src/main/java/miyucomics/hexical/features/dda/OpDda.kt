package miyucomics.hexical.features.dda

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import net.minecraft.util.math.Vec3d

object OpDda : ConstMediaAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val start = args.getVec3(0, argc)
		val direction = args.getVec3(1, argc).normalize()
		if (direction == Vec3d.ZERO)
			return listOf(ListIota(emptyList()), ListIota(emptyList()))
		val length = args.getPositiveInt(2, argc)
		val (positions, normals) = DdaUtils.raycast(start, direction, length)

		return listOf(
			ListIota(positions.map { Vec3Iota(it.toCenterPos()) }),
			ListIota(normals.map(::Vec3Iota))
		)
	}
}