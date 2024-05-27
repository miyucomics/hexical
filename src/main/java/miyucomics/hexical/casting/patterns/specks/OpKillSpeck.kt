package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.SpeckEntity

class OpKillSpeck : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val speck = args.getEntity(0, argc)
		if (speck !is SpeckEntity)
			throw MishapBadEntity.of(speck, "speck")
		speck.kill()
		return listOf()
	}
}