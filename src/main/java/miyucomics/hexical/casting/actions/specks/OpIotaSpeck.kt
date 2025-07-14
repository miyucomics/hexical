package miyucomics.hexical.casting.actions.specks

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.entities.specklikes.SpeckEntity

class OpIotaSpeck : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val speck = args.getEntity(0, argc)
		if (speck !is SpeckEntity)
			throw MishapBadEntity.of(speck, "speck")
		speck.setIota(args[1])
		return listOf()
	}
}