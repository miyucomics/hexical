package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.entities.FleckEntity
import miyucomics.hexical.entities.SpeckEntity
import net.minecraft.util.math.Vec2f

class OpShapeFleck : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val fleck = args.getEntity(0, argc)
		val design = args.getList(0, argc)
		if (design.size() > 25)
			throw MishapInvalidIota.of(args[0], 0, "fleck_vector_list")
		val points = mutableListOf<Vec2f>()
		for (point in design) {
			if (point.type != Vec3Iota.TYPE)
				throw MishapInvalidIota.of(args[0], 0, "fleck_vector_list")
			else {
				val vector = (point as Vec3Iota).vec3
				if (vector.lengthSquared() > 3*3)
					throw MishapInvalidIota.of(args[0], 0, "fleck_vector_list")
				points.add(Vec2f(vector.x.toFloat(), vector.y.toFloat()))
			}
		}
		if (fleck !is FleckEntity)
			throw MishapBadEntity.of(fleck, "fleck")
		fleck.setShape(points)
		return listOf()
	}
}