package miyucomics.hexical.casting.patterns.scroll

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.entities.AnimatedScrollEntity

class OpVanishScroll : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val scroll = args.getEntity(0, argc)
		env.assertEntityInRange(scroll)
		if (scroll !is AnimatedScrollEntity)
			throw MishapBadEntity.of(scroll, "animated_scroll")
		return SpellAction.Result(Spell(scroll), 0, listOf())
	}

	private data class Spell(val scroll: AnimatedScrollEntity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			scroll.toggleVanished()
		}
	}
}