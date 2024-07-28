package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.MeshEntity

class OpReadMesh : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val mesh = args.getEntity(0, argc)
		if (mesh !is MeshEntity)
			throw MishapBadEntity.of(mesh, "mesh")
		return listOf(ListIota(mesh.getShape()))
	}
}