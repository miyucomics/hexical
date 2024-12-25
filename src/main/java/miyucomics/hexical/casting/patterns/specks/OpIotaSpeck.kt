package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.specklikes.SpeckEntity

class OpIotaSpeck : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val speck = args.getEntity(0, argc)
		val iota = args[1]
		if (speck !is SpeckEntity)
			throw MishapBadEntity.of(speck, "speck")
		speck.setIota(iota)
		return listOf()
	}
}