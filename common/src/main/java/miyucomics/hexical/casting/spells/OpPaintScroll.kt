package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.entities.LivingScrollEntity

class OpPaintScroll : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0)
		val patterns = args.getList(1, argc)
		if (entity !is LivingScrollEntity)
			throw MishapBadEntity.of(entity, "living_scroll")
		patterns.forEach {
			if (it.type != HexIotaTypes.PATTERN)
				throw MishapInvalidIota.of(args[1], 1, "pattern_list")
		}
		return Triple(Spell(entity, patterns), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val entity: LivingScrollEntity, val patterns: SpellList) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			entity.patterns = patterns.toList().map { (it as PatternIota).pattern }.toMutableList()
		}
	}
}