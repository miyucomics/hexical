package miyucomics.hexical.features.mute

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.inits.HexicalCardinalComponents

object OpIsMute : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val target = args.getEntity(0, argc)
		env.assertEntityInRange(target)
		return target.getComponent(HexicalCardinalComponents.MUTED_COMPONENT).muted.asActionResult
	}
}