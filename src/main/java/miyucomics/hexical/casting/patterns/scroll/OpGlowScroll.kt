package miyucomics.hexical.casting.patterns.scroll

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.LivingScrollEntity
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d

class OpGlowScroll : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val scroll = args.getEntity(0, argc)
		ctx.assertEntityInRange(scroll)
		if (scroll !is LivingScrollEntity)
			throw MishapBadEntity.of(scroll, "living_scroll")
		return Triple(Spell(scroll), MediaConstants.DUST_UNIT / 2, listOf())
	}

	private data class Spell(val scroll: LivingScrollEntity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			scroll.toggleGlow()
		}
	}
}