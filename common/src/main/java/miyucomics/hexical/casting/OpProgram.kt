package miyucomics.hexical.casting

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions

class OpProgram : SpellAction {
	override val argc = 1

	// DEVELOPMENT USE ONLY
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val patterns = args.getList(0, argc).toList()
		return Triple(Spell(patterns), 0, listOf())
	}

	private data class Spell(val patterns: List<Iota>) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val (handStack, _) = ctx.getHeldItemToOperateOn { IXplatAbstractions.INSTANCE.findHexHolder(it) != null }
			IXplatAbstractions.INSTANCE.findHexHolder(handStack)?.writeHex(patterns, MediaConstants.CRYSTAL_UNIT * 10000)
		}
	}
}