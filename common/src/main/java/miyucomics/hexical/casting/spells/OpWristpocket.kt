package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d

class OpWristpocket : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		// make this cost three dust if adding the item
		// otherwise make it free
		return Triple(Spell(ctx.otherHand), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class Spell(val hand: Hand) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			// disappear the item
		}
	}
}