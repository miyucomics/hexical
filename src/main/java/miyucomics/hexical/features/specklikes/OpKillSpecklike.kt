package miyucomics.hexical.features.specklikes

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity

object OpKillSpecklike : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val speck = args.getEntity(0, argc)
		if (speck !is Specklike)
			throw MishapBadEntity.of(speck, "speck")
		speck.kill()
		return emptyList()
	}
}