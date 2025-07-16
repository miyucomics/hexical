package miyucomics.hexical.casting.actions.scroll

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.animated_scrolls.AnimatedScrollEntity

class OpAlterScroll(val process: (AnimatedScrollEntity) -> Unit) : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val scroll = args.getEntity(0, argc)
		env.assertEntityInRange(scroll)
		if (scroll !is AnimatedScrollEntity)
			throw MishapBadEntity.of(scroll, "animated_scroll")
		return SpellAction.Result(Spell(scroll, process), 0, listOf())
	}

	private data class Spell(val scroll: AnimatedScrollEntity, val process: (AnimatedScrollEntity) -> Unit) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			process(scroll)
		}
	}
}