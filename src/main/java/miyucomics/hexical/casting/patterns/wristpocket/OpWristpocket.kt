package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.util.Hand

class OpWristpocket : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		return Triple(Spell(ctx.otherHand), MediaConstants.DUST_UNIT / 8, listOf())
	}

	private data class Spell(val hand: Hand) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			val item = PersistentStateHandler.getWristpocketStack(ctx.caster)
			PersistentStateHandler.setWristpocketStack(ctx.caster, ctx.caster.getStackInHand(hand))
			ctx.caster.setStackInHand(hand, item)
		}
	}
}