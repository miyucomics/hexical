package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions

class OpProgramLamp : SpellAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val patterns = args.getList(0, argc).toList()
		return Triple(Spell(patterns), 3 * MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val patterns: List<Iota>) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val (handStack, _) = ctx.getHeldItemToOperateOn {
				val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(it)
				hexHolder != null && !hexHolder.hasHex()
			}
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(handStack)
			if (hexHolder != null && !hexHolder.hasHex())
				hexHolder.writeHex(patterns, 100000)
		}
	}
}