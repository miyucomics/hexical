package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class OpCongrats : SpellAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		ctx.assertEntityInRange(target)
		if (target !is ServerPlayerEntity)
			throw MishapBadEntity(target, Text.translatable("text.hexical.congrats.player"))
		return Triple(Spell(target), 2 * MediaConstants.DUST_UNIT, listOf(ParticleSpray.burst(target.pos, 1.0)))
	}

	private data class Spell(val player: ServerPlayerEntity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			ctx.caster.sendMessage(Text.translatable("text.hexical.congrats", player.displayName));
		}
	}
}