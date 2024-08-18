package miyucomics.hexical.casting.patterns.scroll

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.LivingScrollEntity

class OpVanishScroll : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val scroll = args.getEntity(0, argc)
		ctx.assertEntityInRange(scroll)
		if (scroll !is LivingScrollEntity)
			throw MishapBadEntity.of(scroll, "living_scroll")
		return Triple(Spell(scroll), 0, listOf())
	}

	private data class Spell(val scroll: LivingScrollEntity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			scroll.toggleVanished()
		}
	}
}