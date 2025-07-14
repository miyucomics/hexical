package miyucomics.hexical.casting.actions.specks

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.entities.specklikes.MeshEntity

class OpReadMesh : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val mesh = args.getEntity(0, argc)
		if (mesh !is MeshEntity)
			throw MishapBadEntity.of(mesh, "mesh")
		return listOf(ListIota(mesh.getShape()))
	}
}