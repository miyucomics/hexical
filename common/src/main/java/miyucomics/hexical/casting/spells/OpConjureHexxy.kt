package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota

class OpConjureHexxy : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> { return Triple(Spell(), 0, listOf()) }

	private class Spell : RenderedSpell {
		override fun cast(ctx: CastingContext) {

		}
	}
}