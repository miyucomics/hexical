package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import miyucomics.hexical.entities.MeshEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

class OpReweave : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val mesh = args.getEntity(0, argc)
		if (mesh !is MeshEntity)
			throw MishapBadEntity.of(mesh, "mesh")

		val design = args.getList(1, argc)
		if (design.size() > 32)
			throw MishapInvalidIota.of(args[1], 1, "mesh_vector_list")
		val shape = mutableListOf<Vec3d>()
		for (point in design) {
			if (point.type != Vec3Iota.TYPE)
				throw MishapInvalidIota.of(args[1], 1, "mesh_vector_list")
			else {
				val vector = (point as Vec3Iota).vec3
				if (vector.length() > 32)
					throw MishapInvalidIota.of(args[1], 1, "mesh_vector_list")
				shape.add(vector)
			}
		}
		return Triple(Spell(mesh, shape), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val mesh: MeshEntity, val shape: List<Vec3d>) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			mesh.setShape(shape)
		}
	}
}