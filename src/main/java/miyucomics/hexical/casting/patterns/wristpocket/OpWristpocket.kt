package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Hand

class OpWristpocket : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		if (ctx.caster.getStackInHand(ctx.otherHand) != ItemStack.EMPTY && ctx.caster.getStackInHand(ctx.otherHand).item != Items.AIR)
			return Triple(Spell(ctx.otherHand), MediaConstants.DUST_UNIT / 2, listOf())
		return Triple(Spell(ctx.otherHand), MediaConstants.DUST_UNIT / 8, listOf())
	}

	private data class Spell(val hand: Hand) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val item = PersistentStateHandler.wristpocketItem(ctx.caster)
			val current = ctx.caster.getStackInHand(hand)
			PersistentStateHandler.stashWristpocket(ctx.caster, current)
			ctx.caster.setStackInHand(hand, item)
		}
	}
}