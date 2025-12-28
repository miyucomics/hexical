package miyucomics.hexical.features.mute

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalCardinalComponents

object OpMute : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val target = args.getEntity(0, argc)
		env.assertEntityInRange(target)
		val component = target.getComponent(HexicalCardinalComponents.MUTED_COMPONENT)
		return SpellAction.Result(Spell(component), if (component.muted) 0L else MediaConstants.CRYSTAL_UNIT, listOf(ParticleSpray.cloud(target.pos, 1.0)))
	}

	private data class Spell(val component: MutedComponent) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			component.toggleMute()
		}
	}
}