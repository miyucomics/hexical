package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingEnvironmentMinterface

class OpCheckSource(private val source: SpecializedSource) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment) = ((env as CastingEnvironmentMinterface).getSpecializedSource() == source).asActionResult
}