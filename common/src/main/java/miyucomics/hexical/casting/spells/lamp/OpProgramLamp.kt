package miyucomics.hexical.casting.spells.lamp

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions

class OpProgramLamp : SpellAction {
	override val argc = 1

	// Right now, this pattern is extremely overpowered
	// Later it'll be specialized to program only the lamp
	// But for debug purposes, it lets you program any casting item for free
	// And even gives you ten thousand crystals worth of media as a bonus
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